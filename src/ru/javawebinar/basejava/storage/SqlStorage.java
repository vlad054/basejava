package ru.javawebinar.basejava.storage;

import exception.ExistStorageException;
import exception.NotExistStorageException;
import exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
        return sqlHelper.<Resume>execute("SELECT * FROM resume r WHERE r.uuid =?", ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            return new Resume(uuid, rs.getString(2));
        });
    }

    @Override
    public void update(Resume r) {
        sqlHelper.execute("UPDATE resume SET full_name=? WHERE uuid=?", ps -> {
            ps.setString(1, r.getFullName());
            ps.setString(2, r.getUuid());
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(r.getUuid());
            }
            return null;
        });
    }

    @Override
    public void save(Resume r) {
            sqlHelper.execute("INSERT INTO resume (uuid, full_name) VALUES (?,?)", ps -> {
            ps.setString(1, r.getUuid());
            ps.setString(2, r.getFullName());
            ps.executeUpdate();
            return null;
        });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.execute("DELETE FROM resume r WHERE r.uuid =?", ps -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            };
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.<List<Resume>>execute("SELECT * FROM resume ORDER BY 1,2", ps -> {
            ResultSet rs = ps.executeQuery();
            List<Resume> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Resume(rs.getString(1), rs.getString(2)));
            }
            return list;
        });
    }

    @Override
    public int size() {
        return sqlHelper.<Integer>execute("SELECT count(uuid) FROM resume", ps -> {
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new SQLException();
            }
            return rs.getInt(1);
        });
    }

    public class SqlHelper {
        private ConnectionFactory connectionFactory;

        public SqlHelper(String dbUrl, String dbUser, String dbPassword){
            connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        }

        public <T> T execute(String query, BlockCode<T> blockCode) {
            try (
                    Connection conn = connectionFactory.getConnection();
                    PreparedStatement ps = conn.prepareStatement(query)) {
                return blockCode.execute(ps);
            } catch (SQLException e) {
                if ("23505".equals(e.getSQLState())){
                    throw new ExistStorageException("Exist row in DB");
                }
                throw new StorageException(e);
            }
        }
    }

    public interface BlockCode<T>{
         T execute(PreparedStatement preparedStatement) throws SQLException;
    }
}