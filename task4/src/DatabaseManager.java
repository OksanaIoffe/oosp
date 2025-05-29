import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mydatabase";
    private static final String USER = "username";
    private static final String PASS = "password";

    private Connection connection;

    public DatabaseManager() throws SQLException {
        this.connection = DriverManager.getConnection(DB_URL, USER, PASS);
        this.connection.setAutoCommit(false); // Включение управления транзакциями вручную
    }

    public DataModel getDataById(int id) throws SQLException {
        String sql = "SELECT * FROM data_table WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                DataModel data = new DataModel(
                        rs.getInt("id"),
                        rs.getString("content"),
                        rs.getBoolean("read_only")
                );

                // Добавляем в кэш, если данные read-only
                if (data.isReadOnly()) {
                    DataCache.getInstance().addToCache(data);
                }

                return data;
            }
        }
        return null;
    }

    public List<DataModel> getAllData() throws SQLException {
        List<DataModel> dataList = new ArrayList<>();
        String sql = "SELECT * FROM data_table";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                DataModel data = new DataModel(
                        rs.getInt("id"),
                        rs.getString("content"),
                        rs.getBoolean("read_only")
                );

                // Добавляем в кэш, если данные read-only
                if (data.isReadOnly()) {
                    DataCache.getInstance().addToCache(data);
                }

                dataList.add(data);
            }
        }
        return dataList;
    }

    public void updateData(DataModel data) throws SQLException {
        String sql = "UPDATE data_table SET content = ?, read_only = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, data.getContent());
            stmt.setBoolean(2, data.isReadOnly());
            stmt.setInt(3, data.getId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Update failed, no rows affected.");
            }

            // Если данные были в кэше и теперь не read-only, удаляем из кэша
            if (DataCache.getInstance().contains(data.getId()) && !data.isReadOnly()) {
                DataCache.getInstance().removeFromCache(data.getId());
            }
        }
    }

    public void insertData(DataModel data) throws SQLException {
        String sql = "INSERT INTO data_table (id, content, read_only) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, data.getId());
            stmt.setString(2, data.getContent());
            stmt.setBoolean(3, data.isReadOnly());

            stmt.executeUpdate();

            // Добавляем в кэш, если данные read-only
            if (data.isReadOnly()) {
                DataCache.getInstance().addToCache(data);
            }
        }
    }

    public void deleteData(int id) throws SQLException {
        String sql = "DELETE FROM data_table WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();

            // Удаляем из кэша, если данные там были
            if (DataCache.getInstance().contains(id)) {
                DataCache.getInstance().removeFromCache(id);
            }
        }
    }

    public void commit() throws SQLException {
        connection.commit();
    }

    public void rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            System.err.println("Rollback failed: " + e.getMessage());
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Failed to close connection: " + e.getMessage());
        }
    }
}