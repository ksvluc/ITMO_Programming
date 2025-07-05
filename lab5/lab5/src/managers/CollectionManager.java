package managers;

import models.Route;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Класс для управления коллекцией объектов Route.
 */
public class CollectionManager {
    private long currentId = 1;
    private Map<Long, Route> routesMap = new HashMap<>();
    private LinkedList<Route> collection = new LinkedList<>();
    private LocalDateTime lastInitTime;
    private LocalDateTime lastSaveTime;
    private final DumpManager dumpManager;

    public CollectionManager(DumpManager dumpManager) {
        this.lastInitTime = null;
        this.lastSaveTime = null;
        this.dumpManager = dumpManager;
    }

    /**
     * @return Последнее время инициализации.
     */
    public LocalDateTime getLastInitTime() {
        return lastInitTime;
    }

    /**
     * @return Последнее время сохранения.
     */
    public LocalDateTime getLastSaveTime() {
        return lastSaveTime;
    }

    /**
     * @return коллекция.
     */
    public LinkedList<Route> getCollection() {
        return collection;
    }

    /**
     * Получить Route по ID
     */
    public Route byId(long id) {
        return routesMap.get(id);
    }

    /**
     * Содержит ли коллекция Route
     */
    public boolean contains(Route route) {
        return route != null && byId(route.getId()) != null;
    }

    /**
     * Получить свободный ID
     */
    public long getFreeId() {
        while (byId(++currentId) != null);
        return currentId;
    }

    /**
     * Добавляет Route
     */
    public boolean add(Route route) {
        if (contains(route)) return false;
        routesMap.put(route.getId(), route);
        collection.add(route);
        update();
        return true;
    }

    /**
     * Обновляет Route
     */
    public boolean update(Route route) {
        if (!contains(route)) return false;
        collection.remove(byId(route.getId()));
        routesMap.put(route.getId(), route);
        collection.add(route);
        update();
        return true;
    }

    /**
     * Удаляет Route по ID
     */
    public boolean remove(long id) {
        var route = byId(id);
        if (route == null) return false;
        routesMap.remove(route.getId());
        collection.remove(route);
        update();
        return true;
    }

    /**
     * Фиксирует изменения коллекции
     */
    public void update() {
        Collections.sort(collection);
    }

    /**
     * Инициализирует коллекцию из файла
     */
    public boolean init() {
        collection.clear();
        routesMap.clear();
        dumpManager.readCollection(collection);
        lastInitTime = LocalDateTime.now();
        for (var route : collection) {
            if (byId(route.getId()) != null) {
                collection.clear();
                routesMap.clear();
                return false;
            } else {
                if (route.getId() > currentId) currentId = route.getId();
                routesMap.put(route.getId(), route);
            }
        }
        update();
        return true;
    }

    /**
     * Сохраняет коллекцию в файл
     */
    public void saveCollection() {
        dumpManager.writeCollection(collection);
        lastSaveTime = LocalDateTime.now();
    }

    @Override
    public String toString() {
        if (collection.isEmpty()) return "Коллекция пуста!";

        StringBuilder info = new StringBuilder();
        for (var route : collection) {
            info.append(route).append("\n\n");
        }
        return info.toString().trim();
    }
}
