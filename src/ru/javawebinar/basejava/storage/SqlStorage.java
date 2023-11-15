package ru.javawebinar.basejava.storage;

import exception.NotExistStorageException;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.sql.SqlHelper;
import ru.javawebinar.basejava.util.JsonParser;

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
        return sqlHelper.transactionalExecute(conn -> {
            Resume r;
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume WHERE uuid =?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new NotExistStorageException(uuid);
                }
                r = new Resume(uuid, rs.getString("full_name"));
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM contact WHERE resume_uuid =?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    addContact(r, rs);
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM section WHERE resume_uuid =?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    addSection(r, rs);
                }
            }
            return r;
        });
    }

    @Override
    public void update(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid  = ?")) {
                ps.setString(1, r.getFullName());
                ps.setString(2, r.getUuid());
                if (ps.executeUpdate() == 0) {
                    throw new NotExistStorageException(r.getUuid());
                }
                deleteContact(r.getUuid(), conn);
                insertContacts(r, conn);
                deleteSection(r.getUuid(), conn);
                insertSections(r, conn);

                return null;
            }
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
                    insertSections(r, conn);
                    return null;
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
        return sqlHelper.transactionalExecute(
                conn -> {
                    final Map<String, Resume> resumeMap = new LinkedHashMap<>();

                    try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume order by full_name, uuid")) {
                        final ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            final String uuid = rs.getString("uuid");
                            resumeMap.put(uuid, new Resume(uuid, rs.getString("full_name")));
                        }
                    }

                    try (PreparedStatement ps = conn.prepareStatement(
                            "SELECT resume_uuid, value AS value, type AS type FROM contact")) {
                        final ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            final Resume r = resumeMap.get(rs.getString("resume_uuid"));
                            addContact(r, rs);
                        }
                    }

                    try (PreparedStatement ps = conn.prepareStatement(
                            "SELECT resume_uuid, value AS value, type AS type FROM section")) {
                        final ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            final Resume r = resumeMap.get(rs.getString("resume_uuid"));
                            addSection(r, rs);
                        }
                    }

                    return new ArrayList<>(resumeMap.values());
                }
        );
    }

    @Override
    public int size() {
        return sqlHelper.<Integer>execute("SELECT count(uuid) FROM resume", ps -> {
            ResultSet rs = ps.executeQuery();
            return !rs.next() ? 0 : rs.getInt(1);
        });
    }

    private void addContact(Resume r, ResultSet rs) throws SQLException {
        final String value = rs.getString("value");
        if (value != null) {
            r.addContact(ContactType.valueOf(rs.getString("type")), value);
        }
    }

    private void addSection(Resume r,  ResultSet rs) throws SQLException {
        final String value = rs.getString("value");
        if (value != null) {
            SectionType sectionType = SectionType.valueOf(rs.getString("type"));

            r.addSection(sectionType, JsonParser.read(value, AbstractSection.class));
        }
    }

    private void insertSections(Resume r, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO section (value, type, resume_uuid) VALUES (?,?,?)")) {
            for (Map.Entry<SectionType, AbstractSection> e : r.getSections().entrySet()) {
                ps.setString(1, JsonParser.write(e.getValue(), AbstractSection.class));
                ps.setString(2, e.getKey().name());
                ps.setString(3, r.getUuid());
                ps.addBatch();

            }
            ps.executeBatch();
        }
    }

    private void insertContacts(Resume r, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (value, type, resume_uuid) VALUES (?,?,?)")) {
            for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
                ps.setString(1, e.getValue());
                ps.setString(2, e.getKey().name());
                ps.setString(3, r.getUuid());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void deleteContact(String uuidId, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM contact WHERE resume_uuid =?")) {
            ps.setString(1, uuidId);
            ps.executeUpdate();
        }
    }

    private void deleteSection(String uuidId, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM section WHERE resume_uuid =?")) {
            ps.setString(1, uuidId);
            ps.executeUpdate();
        }
    }
}