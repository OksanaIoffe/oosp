import java.util.HashMap;
import java.util.Map;

public class DataCache {
    private static DataCache instance;
    private final Map<Integer, DataModel> cache;

    private DataCache() {
        cache = new HashMap<>();
    }

    public static synchronized DataCache getInstance() {
        if (instance == null) {
            instance = new DataCache();
        }
        return instance;
    }

    public void addToCache(DataModel data) {
        if (data.isReadOnly()) {
            cache.put(data.getId(), data);
        }
    }

    public DataModel getFromCache(int id) {
        return cache.get(id);
    }

    public void removeFromCache(int id) {
        cache.remove(id);
    }

    public void clearCache() {
        cache.clear();
    }

    public boolean contains(int id) {
        return cache.containsKey(id);
    }
}