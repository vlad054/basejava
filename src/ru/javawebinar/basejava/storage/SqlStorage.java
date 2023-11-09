package ru.javawebinar.basejava.storage;

import exception.NotExistStorageException;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SqlStorage implements Storage {

    private static SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        sqlHelper = new SqlHelper(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void clear() {
        sqlHelper.execute("DELETE FROM resume", ps -> {
            ps.execute();
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        Resume resume = getSubject(null, uuid, "contact");
        resume = getSubject(resume, uuid, "section");
        return resume;
    }

    @Override
    public void update(Resume r) {
        sqlHelper.execute("UPDATE resume SET full_name = ? WHERE uuid  =?", ps -> {
            ps.setString(1, r.getFullName());
            ps.setString(2, r.getUuid());
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(r.getUuid());
            }
            return null;
        });
        deleteIn("contact", "resume_uuid", r.getUuid());
        deleteIn("section", "resume_uuid", r.getUuid());
        sqlHelper.transactionalExecute(conn -> insertContacts(r, conn));
        sqlHelper.transactionalExecute(conn -> insertSections(r, conn));
    }

    @Override
    public void save(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
                    try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (full_name, uuid) VALUES (?,?)")) {
                        ps.setString(1, r.getFullName());
                        ps.setString(2, r.getUuid());
                        if (ps.executeUpdate() == 0) {
                            throw new NotExistStorageException(r.getUuid());
                        }
                    }
                    insertContacts(r, conn);
                    return insertSections(r, conn);
                }
        );
    }

    @Override
    public void delete(String uuid) {
        deleteIn("resume", "uuid", uuid);
    }

    @Override
    public List<Resume> getAllSorted() {
        final Map<String, Resume> resumeMap = new LinkedHashMap<>();
        sqlHelper.execute("""
                        SELECT r.uuid, r.full_name, c.type, c.value, 'contact' typein
                        FROM resume r LEFT JOIN contact c ON r.uuid = c.resume_uuid
                        UNION
                        SELECT r.uuid, r.full_name, s.type, s.value, 'section' typein
                        FROM resume r LEFT JOIN section s ON r.uuid = s.resume_uuid
                        ORDER BY full_name, uuid
                        """,
                ps -> {
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        final String uuid = rs.getString("uuid");
                        final Resume r;
                        if (!resumeMap.containsKey(uuid)) {
                            r = new Resume(uuid, rs.getString("full_name"));
                            resumeMap.put(uuid, r);
                        } else {
                            r = resumeMap.get(uuid);
                        }

                        if (rs.getString("typein").equals("contact")) {
                            addContact(r, rs.getString("value"), rs.getString("type"));
                        } else if (rs.getString("typein").equals("section")) {
                            addSection(r, rs.getString("value"), rs.getString("type"));
                        }
                    }
                    return null;
                });
        return new ArrayList<>(resumeMap.values());
    }

    @Override
    public int size() {
        return sqlHelper.<Integer>execute("SELECT count(uuid) FROM resume", ps -> {
            ResultSet rs = ps.executeQuery();
            return !rs.next() ? 0 : rs.getInt(1);
        });
    }

    public interface BlockCode<T> {
        T execute(PreparedStatement preparedStatement) throws SQLException;
    }

    private Resume getSubject(Resume resume, String uuid, String subjectType) {

        Resume finalResume = resume;
        resume = sqlHelper.execute("SELECT r.uuid, r.full_name, c.type, c.value " +
                " FROM resume r LEFT JOIN " + subjectType + " c ON r.uuid = c.resume_uuid " +
                " WHERE r.uuid =?", ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            Resume r;
            if (finalResume == null) {
                r = new Resume(uuid, rs.getString("full_name"));
            } else {
                r = finalResume;
            }
            do {
                if (subjectType.equals("contact")) {
                    addContact(r, rs.getString("value"), rs.getString("type"));
                } else if (subjectType.equals("section")) {
                    addSection(r, rs.getString("value"), rs.getString("type"));
                }
            } while (rs.next());
            return r;
        });
        return resume;
    }

    private void addContact(Resume r, String value, String type) {
        if (value != null) {
            ContactType contactType = ContactType.valueOf(type);
            r.addContact(contactType, value);
        }
    }

    private void addSection(Resume r, String value, String type) {
        if (value != null) {
            SectionType sectionType = SectionType.valueOf(type);

            switch (sectionType) {
                case OBJECTIVE, PERSONAL -> r.addSection(sectionType, new TextSection(value));
                case ACHIEVEMENT, QUALIFICATIONS -> {
                    if (r.getSections().containsKey(sectionType)) {
                        ListSection l = (ListSection) r.getSections().get(sectionType);
                        l.getListSection().add(value);
                    } else {
                        List<String> strings = new ArrayList<>();
                        strings.add(value);
                        r.getSections().put(sectionType, new ListSection(strings));
                    }
                }
            }
        }
    }

    private Object insertSections(Resume r, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO section (value, type, resume_uuid) VALUES (?,?,?)")) {
            for (Map.Entry<SectionType, AbstractSection> e : r.getSections().entrySet()) {
                if (e.getKey() == SectionType.OBJECTIVE || e.getKey() == SectionType.PERSONAL) {
                    ps.setString(1, e.getValue().toString());
                    ps.setString(2, e.getKey().name());
                    ps.setString(3, r.getUuid());
                    ps.addBatch();

                } else if (e.getKey() == SectionType.ACHIEVEMENT || e.getKey() == SectionType.QUALIFICATIONS) {
                    ListSection listSection = (ListSection) e.getValue();
                    for (String str : listSection.getListSection()) {
                        ps.setString(1, str);
                        ps.setString(2, e.getKey().name());
                        ps.setString(3, r.getUuid());
                        ps.addBatch();
                    }
                }
            }
            ps.executeBatch();
            return null;
        }
    }

    private Object insertContacts(Resume r, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (value, type, resume_uuid) VALUES (?,?,?)")) {
            for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
                ps.setString(1, e.getValue());
                ps.setString(2, e.getKey().name());
                ps.setString(3, r.getUuid());
                ps.addBatch();
            }
            ps.executeBatch();
            return null;
        }
    }

    private void deleteIn(String type, String uuid, String uuidId) {
        sqlHelper.execute("DELETE FROM " + type + " WHERE " + uuid + " =?", ps -> {
            ps.setString(1, uuidId);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(uuidId);
            }
            return null;
        });
    }

}