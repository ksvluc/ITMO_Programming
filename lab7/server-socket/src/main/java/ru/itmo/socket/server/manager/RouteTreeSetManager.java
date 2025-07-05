package ru.itmo.socket.server.manager;


import ru.itmo.socket.common.dto.UserDto;
import ru.itmo.socket.common.entity.Route;
import ru.itmo.socket.server.concurrent.UserContext;
import ru.itmo.socket.server.db.DatabaseConfig;
import ru.itmo.socket.server.db.dao.RouteDao;
import ru.itmo.socket.server.db.dao.UsersDao;
import ru.itmo.socket.server.db.exception.SqlRequestException;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class RouteTreeSetManager {
    private final RouteDao RouteDao = new RouteDao();
    private final UsersDao usersDao = new UsersDao();

    private static RouteTreeSetManager instance;
    // dbUserId -> Set<Route>
    private Map<Integer, SortedSet<Route>> routesMap;
    private LocalDateTime initializationTime;

    // Приватный конструктор для реализации singleton
    private RouteTreeSetManager() {
        this.routesMap = new HashMap<>();
        this.initializationTime = LocalDateTime.now();
    }

    // Глобальная точка доступа к экземпляру
    public static synchronized RouteTreeSetManager getInstance() {
        if (instance == null) {
            instance = new RouteTreeSetManager();
        }
        return instance;
    }

    // Новый метод: проверяет, есть ли маршрут с данным id у текущего пользователя
    public synchronized boolean existsById(long id) {
        return getCollectionOfCurrentUser().stream()
                .anyMatch(rt -> rt.getId() == id);
    }

    @Deprecated // was used for save command
    public synchronized SortedSet<Route> getAllElements() {
        return getCollectionOfCurrentUser();
    }


    // Метод для получения информации о коллекции
    public synchronized String getCollectionInfo() {
        SortedSet<Route> routes = getCollectionOfCurrentUser();
        return "Тип коллекции: " + routes.getClass().getName() + "\n" +
                "Дата инициализации: " + initializationTime + "\n" +
                "Количество элементов: " + routes.size();
    }


    // Метод для получения строкового представления всех элементов коллекции
    public synchronized String getAllElementsAsString() {
        if (routesMap.isEmpty()) {
            return "Коллекция пуста.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        routesMap.forEach((userId, routes) -> {
                    UserDto user = usersDao.findById(userId);
                    sb.append(("USER %s: %n" +
                            "%s%n" +
                            "----------------%n %n")
                            .formatted(user.getLogin(), routes));
                }
        );


        sb.append("\n]");
        return sb.toString();
    }

    // Добавление нового элемента в коллекцию
    public synchronized boolean add(Route element) {
        SortedSet<Route> routes = getCollectionOfCurrentUser();
        return trySaveUserToDb(element) && routes.add(element);
    }

    // Обновление элемента с указанным id: безопасное удаление старого элемента и добавление нового (с сохранённым id)
    public synchronized boolean updateById(long id, Route newElement) {
        // причем обновляем мы ТОЛЬКО свои элементы
        SortedSet<Route> routes = getCollectionOfCurrentUser();

        Iterator<Route> iterator = routes.iterator();
        while (iterator.hasNext()) {
            Route rt = iterator.next();
            if (rt.getId() == id) {
                RouteDao.remove(id);
                iterator.remove();
                newElement.setId(id);
                return tryUpdateInDb(newElement) && routes.add(newElement);
            }
        }
        return false;
    }


    // Удаление элемента по id
    public synchronized boolean removeById(long id) {
        SortedSet<Route> routes = getCollectionOfCurrentUser();
        boolean removeIf = routes.removeIf(rt -> rt.getId() == id);
        if (removeIf){
            RouteDao.remove(id);
        }
        return removeIf;

    }

    // Очистка всей коллекции
    public synchronized void clear() {
        SortedSet<Route> routes = getCollectionOfCurrentUser();
        routes.clear();
    }

    // Добавление элемента, если он больше наибольшего элемента текущей коллекции
    public synchronized Route head() {
        SortedSet<Route> routes = getCollectionOfCurrentUser();
        if (routes.isEmpty()) {
            return null;
        }
        Route first = routes.first();
        routes.remove(first);
        return first;
    }

    // Удаление всех элементов, меньших заданного (возвращает количество удалённых элементов)
    public synchronized int removeLower(Route element) {
        SortedSet<Route> routes = getCollectionOfCurrentUser();
        int initialSize = routes.size();
        routes.removeIf(rt -> rt.compareTo(element) < 0);
        int newSize = routes.size();
        return initialSize - newSize;
    }

    // Вывод элемента с максимальным значением поля creationDate
    public Route getElementWithMaxCreationDate() {
        SortedSet<Route> routes = getCollectionOfCurrentUser();
        return routes.stream()
                .max(Comparator.comparing(Route::getCreationDate))
                .orElseThrow(() -> new NoSuchElementException("Коллекция пуста"));
    }

    // Метод для получения элементов в порядке убывания
    public synchronized String getElementsDescending() {
        TreeSet<Route> routes = (TreeSet<Route>) getCollectionOfCurrentUser();
        if (routes.isEmpty()) {
            return "Коллекция пуста.";
        }
        StringBuilder sb = new StringBuilder();
        Iterator<Route> iterator = routes.descendingSet().iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.next().toString());
            if (iterator.hasNext()) {
                sb.append(System.lineSeparator());
            }
        }
        return sb.toString();
    }


    // Получение уникальных значений поля distance у всех элементов коллекции
    public Set<Integer> getUniqueDistances() {
        SortedSet<Route> routes = getCollectionOfCurrentUser();
        return routes.stream()
                .map(Route::getDistance)  // Преобразуем Route в Integer (distance)
                .filter(Objects::nonNull) // Фильтруем ненулевые расстояния
                .collect(Collectors.toSet());
    }

    private boolean trySaveUserToDb(Route element) {
        try {
            if (element.getCreationDate() == null) {
                element.setCreationDate(LocalDateTime.now());
            }
            RouteDao.insert(element, UserContext.getDbUserId());
            return true;
        } catch (SqlRequestException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean tryUpdateInDb(Route element) {
        try {
            RouteDao.update(element, UserContext.getDbUserId());
            return true;
        } catch (SqlRequestException e) {
            e.printStackTrace();
            return false;
        }
    }

    private SortedSet<Route> getCollectionOfCurrentUser() {
        Integer dbUserId = UserContext.getDbUserId();
        SortedSet<Route> collection = routesMap.get(dbUserId);

        if (collection == null) {
            collection = new TreeSet<>();
            routesMap.put(dbUserId, collection);
        }
        return collection;
    }

    public void fetchInitialDataFromDb() {
        try (Connection connection = DatabaseConfig.getConnection()) {
            List<UserDto> users = usersDao.findAll(connection);
            for (UserDto userDto : users) {
                List<Route> routes = RouteDao.findAllByUserId(connection, userDto.getId());
                routesMap.put(userDto.getId(), new TreeSet<>(routes));
            }

            List<String> userRepresentation = users.stream()
                    .map(userDto -> "{id = %d, login = %s}".formatted(userDto.getId(), userDto.getLogin()))
                    .toList();

            System.out.println("Ура ура! Загружены элементы из БД для пользователей:"
                    + userRepresentation);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


}
