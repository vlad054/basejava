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
        return sqlHelper.execute("""
                        SELECT r.uuid, r.full_name, c.type, c.value, 'contact' typein
                        FROM resume r LEFT JOIN contact c ON r.uuid = c.resume_uuid
                        WHERE r.uuid =?
                        UNION
                        SELECT r.uuid, r.full_name, s.type, s.value, 'section' typein
                        FROM resume r LEFT JOIN section s ON r.uuid = s.resume_uuid
                        WHERE r.uuid =?
                        """
                        , ps -> {
            ps.setString(1, uuid);
            ps.setString(2, uuid);

            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            Resume r = new Resume(uuid, rs.getString("full_name"));

            do {
                if (rs.getString("typein").equals("contact")) {
                    addContact(r, rs.getString("value"), rs.getString("type"));
                } else if (rs.getString("typein").equals("section")) {
                    addSection(r, rs.getString("value"), rs.getString("type"));
                }
            } while (rs.next());
            return r;
        });
    }

    @Override
    public void update(Resume r) {
        sqlHelper.execute("UPDATE resume SET full_name = ? WHERE uuid  =?", ps -> {
            ps.setString(1, r.getFullName());
            ps.setString(2, r.getUuid());
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(r.getUuid());
            }
            deleteContact(r.getUuid());
            deleteSection(r.getUuid());
            sqlHelper.transactionalExecute(conn -> insertContacts(r, conn));
            sqlHelper.transactionalExecute(conn -> insertSections(r, conn));
            return null;
        });
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
        sqlHelper.execute("DELETE FROM resume WHERE uuid =?", ps -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
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
                    List<String> strings = Arrays.asList(value.split("\n"));
                    r.addSection(sectionType, new ListSection(strings));
                }
            }
        }
    }

    private Object insertSections(Resume r, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO section (value, type, resume_uuid) VALUES (?,?,?)")) {
            for (Map.Entry<SectionType, AbstractSection> e : r.getSections().entrySet()) {

                switch (e.getKey()){
                    case OBJECTIVE, PERSONAL ->{
                        ps.setString(1, e.getValue().toString());
                        ps.setString(2, e.getKey().name());
                        ps.setString(3, r.getUuid());
                        ps.addBatch();
                    }
                    case ACHIEVEMENT, QUALIFICATIONS -> {
                        ListSection listSection = (ListSection) e.getValue();
                        String str = String.join("\n", listSection.getListSection());
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

    private void deleteContact(String uuidId) {
        sqlHelper.execute("DELETE FROM contact WHERE resume_uuid =?", ps -> {
            ps.setString(1, uuidId);
            ps.executeUpdate();
            return null;
        });
    }

    private void deleteSection(String uuidId) {
        sqlHelper.execute("DELETE FROM section WHERE resume_uuid =?", ps -> {
            ps.setString(1, uuidId);
            ps.executeUpdate();
            return null;
        });
    }
}