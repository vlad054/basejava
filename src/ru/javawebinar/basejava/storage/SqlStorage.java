package ru.javawebinar.basejava.storage;

import exception.ExistStorageException;
import exception.NotExistStorageException;
import exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SqlStorage implements Storage {
    public final ConnectionFactory connectionFactory;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

//    @Override
//    public void clear() {
//        try (Connection conn = connectionFactory.getConnection();
//             PreparedStatement ps = conn.prepareStatement("DELETE FROM resume")) {
//            ps.execute();
//        } catch (SQLException e) {
//            throw new StorageException(e);
//        }
//    }

    @Override
    public void clear() {
        new SqlHelper().execute("DELETE FROM resume", ps -> {
            ps.execute();
            return null;
        });
    }

//    @Override
//    public Resume get(String uuid) {
//        try (Connection conn = connectionFactory.getConnection();
//             PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume r WHERE r.uuid =?")) {
//            ps.setString(1, uuid);
//            ResultSet rs = ps.executeQuery();
//            if (!rs.next()) {
//                throw new NotExistStorageException(uuid);
//            }
//            return new Resume(uuid, rs.getString(2));
//        } catch (SQLException e) {
//            throw new StorageException(e);
//        }
//    }

    @Override
    public Resume get(String uuid) {
        return (Resume) new SqlHelper().execute("SELECT * FROM resume r WHERE r.uuid =?", ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            return new Resume(uuid, rs.getString(2));
        });
    }

//    @Override
//    public void update(Resume r) {
//        try (Connection conn = connectionFactory.getConnection()) {
//            PreparedStatement ps = conn.prepareStatement("UPDATE resume SET full_name=? WHERE uuid=?");
//            ps.setString(1, r.getFullName());
//            ps.setString(2, r.getUuid());
//            if (ps.executeUpdate() == 0) {
//                throw new NotExistStorageException(r.getUuid());
//            }
//        } catch (SQLException e) {
//            throw new NotExistStorageException(r.getUuid());
//        }
//    }

    @Override
    public void update(Resume r) {
        try {
            new SqlHelper().execute("UPDATE resume SET full_name=? WHERE uuid=?", ps -> {
                ps.setString(1, r.getFullName());
                ps.setString(2, r.getUuid());
                if (ps.executeUpdate() == 0) {
                    throw new NotExistStorageException(r.getUuid());
                }
                return null;
            });
        } catch (StorageException e) {
            throw new NotExistStorageException(r.getUuid());
        }
    }

//    @Override
//    public void save(Resume r) {
//        try (Connection conn = connectionFactory.getConnection();
//             PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
//            ps.setString(1, r.getUuid());
//            ps.setString(2, r.getFullName());
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            throw new ExistStorageException(r.getUuid());
//        }
//
//    }

    @Override
    public void save(Resume r) {
        try {
            new SqlHelper().execute("INSERT INTO resume (uuid, full_name) VALUES (?,?)", ps -> {
                ps.setString(1, r.getUuid());
                ps.setString(2, r.getFullName());
                ps.executeUpdate();
                return null;
            });
        } catch (StorageException e) {
            throw new ExistStorageException(r.getUuid());
        }
    }


//    @Override
//    public void delete(String uuid) {
//        try (Connection conn = connectionFactory.getConnection();
//             PreparedStatement ps = conn.prepareStatement("DELETE FROM resume r WHERE r.uuid =?")) {
//            ps.setString(1, uuid);
//            if (ps.executeUpdate() == 0) {
//                throw new NotExistStorageException(uuid);
//            }
//        } catch (SQLException e) {
//            throw new NotExistStorageException(uuid);
//        }
//    }

    @Override
    public void delete(String uuid) {
        try {
            new SqlHelper().execute("DELETE FROM resume r WHERE r.uuid =?", ps -> {
                ps.setString(1, uuid);
                if (ps.executeUpdate() == 0) {
                    throw new NotExistStorageException(uuid);
                }
                ;
                return null;
            });
        } catch (StorageException e) {
            throw new NotExistStorageException(uuid);
        }
    }

//    @Override
//    public List<Resume> getAllSorted() {
//        try (Connection conn = connectionFactory.getConnection();
//             PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume")) {
//            ResultSet rs = ps.executeQuery();
//            List<Resume> list = new ArrayList<>();
//            while (rs.next()) {
//                list.add(new Resume(rs.getString(1), rs.getString(2)));
//            }
//            list.sort(Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid));
//            return list;
//        } catch (SQLException e) {
//            throw new StorageException(e);
//        }
//    }

    @Override
    public List<Resume> getAllSorted() {
        try {
            return (List<Resume>) new SqlHelper().execute("SELECT * FROM resume", ps -> {
                ResultSet rs = ps.executeQuery();
                List<Resume> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(new Resume(rs.getString(1), rs.getString(2)));
                }
                list.sort(Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid));
                return list;
            });
        } catch (ClassCastException e) {
            throw new StorageException(e);
        }
    }

//    @Override
//    public int size() {
//        try (Connection conn = connectionFactory.getConnection();
//             PreparedStatement ps = conn.prepareStatement("SELECT count(uuid) FROM resume")) {
//            ResultSet rs = ps.executeQuery();
//            if (!rs.next()) {
//                throw new SQLException();
//            }
//            return rs.getInt(1);
//        } catch (SQLException e) {
//            throw new StorageException(e);
//        }
//    }

    @Override
    public int size() {
        return (int) new SqlHelper().execute("SELECT count(uuid) FROM resume", ps -> {
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new SQLException();
            }
            return rs.getInt(1);
        });
    }

    public class SqlHelper {
        public Object execute(String query, BlockCode blockCode) {
            try (
                    Connection conn = connectionFactory.getConnection();
                    PreparedStatement ps = conn.prepareStatement(query)) {
                return blockCode.execute(ps);
            } catch (SQLException e) {
                throw new StorageException(e);
            }
        }
    }

    public interface BlockCode {
        Object execute(PreparedStatement preparedStatement) throws SQLException;
    }

}