public class DataAPI {
    private final DatabaseManager dbManager;

    public DataAPI() throws SQLException {
        this.dbManager = new DatabaseManager();
    }

    public DataModel getData(int id) throws DataAccessException {
        try {
            // Сначала проверяем кэш
            DataModel cachedData = DataCache.getInstance().getFromCache(id);
            if (cachedData != null) {
                return cachedData;
            }

            // Если нет в кэше, запрашиваем из БД
            return dbManager.getDataById(id);
        } catch (SQLException e) {
            dbManager.rollback();
            throw new DataAccessException("Failed to get data with id: " + id, e);
        }
    }

    public void updateData(DataModel data) throws DataAccessException {
        try {
            dbManager.updateData(data);
            dbManager.commit();
        } catch (SQLException e) {
            dbManager.rollback();
            throw new DataAccessException("Failed to update data with id: " + data.getId(), e);
        }
    }

    public void insertData(DataModel data) throws DataAccessException {
        try {
            dbManager.insertData(data);
            dbManager.commit();
        } catch (SQLException e) {
            dbManager.rollback();
            throw new DataAccessException("Failed to insert data with id: " + data.getId(), e);
        }
    }

    public void deleteData(int id) throws DataAccessException {
        try {
            dbManager.deleteData(id);
            dbManager.commit();
        } catch (SQLException e) {
            dbManager.rollback();
            throw new DataAccessException("Failed to delete data with id: " + id, e);
        }
    }

    public List<DataModel> generateReport() throws DataAccessException {
        try {
            return dbManager.getAllData();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to generate report", e);
        }
    }

    public void close() {
        dbManager.close();
    }
}