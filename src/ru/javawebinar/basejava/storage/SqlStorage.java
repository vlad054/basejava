package ru.javawebinar.basejava.storage;

import exception.NotExistStorageException;
import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SqlStorage implements Storage {

    private static SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
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
        return sqlHelper.execute("SELECT r.uuid, r.full_name, c.type, c.value " +
                " FROM resume r LEFT JOIN contact c ON r.uuid = c.resume_uuid " +
                " WHERE r.uuid =?", ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            Resume r = new Resume(uuid, rs.getString("full_name"));
            do {
                addContact(r, rs.getString("value"), rs.getString("type"));
            } while (rs.next());
            return r;
        });
    }

    @Override
    public void update(Resume r) {
        sqlHelper.execute("DELETE FROM contact c WHERE c.resume_uuid =?", ps -> {
            ps.setString(1, r.getUuid());
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(r.getUuid());
            }
            return null;
        });

        sqlHelper.transactionalExecute(conn -> getObject(r, conn)
        );
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
                    return getObject(r, conn);
                }
        );
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.execute("DELETE FROM resume r WHERE r.uuid =?", ps -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.execute("""
                                            SELECT r.uuid, r.full_name,
                                            c.type, c.value
                                            FROM resume r 
                                           LEFT JOIN contact c 
                                            ON r.uuid = c.resume_uuid
                                            ORDER BY r.full_name, r.uuid
                                            """,
                ps -> {
                    ResultSet rs = ps.executeQuery();
//                    Resume r;
                    final Map<String, Resume> resumeMap = new LinkedHashMap<>();

                    while (rs.next()) {
                        final String uuid = rs.getString("uuid");
                        final Resume r;

                        if (resumeMap.containsKey(uuid)){
                            r = resumeMap.get(uuid);
                        } else {
                            r = new Resume(uuid, rs.getString("full_name"));
                            resumeMap.put(uuid, r);
                        }
                        addContact(r, rs.getString("value"), rs.getString("type"));
                    }
                    return new ArrayList<>(resumeMap.values());
                });
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

    private Object getObject(Resume r, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (value, type, resume_uuid) VALUES (?,?,?)")) {
            for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
                ps.setString(1, e.getValue());
                ps.setString(2, e.getKey().name());
                ps.setString(3, r.getUuid());
                ps.addBatch();
            }
            ps.executeBatch();
        }
        return null;
    }

}