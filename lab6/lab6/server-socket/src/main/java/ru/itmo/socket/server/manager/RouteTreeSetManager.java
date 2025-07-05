package ru.itmo.socket.server.manager;


import ru.itmo.socket.common.entity.Route;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class RouteTreeSetManager {
    private static RouteTreeSetManager instance;
    private TreeSet<Route> routes;
    private LocalDateTime initializationTime;

    // Приватный конструктор для реализации singleton
    private RouteTreeSetManager() {
        this.routes = new TreeSet<>();
        this.initializationTime = LocalDateTime.now();
    }

    // Глобальная точка доступа к экземпляру
    public static synchronized RouteTreeSetManager getInstance() {
        if (instance == null) {
            instance = new RouteTreeSetManager();
        }
        return instance;
    }

    public TreeSet<Route> getAllElements() {
        return routes;
    }


    // Метод для получения информации о коллекции
    public String getCollectionInfo() {
        return "Тип коллекции: " + routes.getClass().getName() + "\n" +
                "Дата инициализации: " + initializationTime + "\n" +
                "Количество элементов: " + routes.size();
    }


    // Метод для получения строкового представления всех элементов коллекции
    public String getAllElementsAsString() {
        if (routes.isEmpty()) {
            return "Коллекция пуста.";
        }
        StringBuilder sb = new StringBuilder();
        Iterator<Route> iterator = routes.iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.next().toString());
            if (iterator.hasNext()) {
                sb.append(System.lineSeparator());
            }
        }
        return sb.toString();
    }

    // Добавление нового элемента в коллекцию
    public boolean add(Route element) {
        return routes.add(element);
    }

    // Обновление элемента с указанным id: безопасное удаление старого элемента и добавление нового (с сохранённым id)
    public boolean updateById(long id, Route newElement) {
        Iterator<Route> iterator = routes.iterator();
        while (iterator.hasNext()) {
            Route lw = iterator.next();
            if (lw.getId() == id) {
                iterator.remove();
                newElement.setId(id);
                routes.add(newElement);
                return true;
            }
        }
        return false;
    }

    public boolean containsId(long id) {
        return routes.stream().anyMatch(lw -> lw.getId() == id);
    }

    // Удаление элемента по id
    public boolean removeById(long id) {
        return routes.removeIf(lw -> lw.getId() == id);
    }

    // Очистка всей коллекции
    public void clear() {
        routes.clear();
    }

    // Извлекает и удаляет первый элемент коллекции
    public Route head() {
        if (routes.isEmpty()) {
            return null;
        }
        Route first = routes.first();
        routes.remove(first);
        return first;
    }

    // Удаление всех элементов, меньших заданного (возвращает количество удалённых элементов)
    public void removeLower(Route element) {
        int initialSize = routes.size();
        routes.removeIf(lw -> lw.compareTo(element) < 0);
    }

    // Вывод элемента с максимальным значением поля creationDate
    public Route getElementWithMaxCreationDate() {
        return routes.stream()
                .max(Comparator.comparing(Route::getCreationDate))
                .orElseThrow(() -> new NoSuchElementException("Коллекция пуста"));
    }

    // Метод для получения элементов в порядке убывания
    public String getElementsDescending() {
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
        return routes.stream()
                .map(Route::getDistance)  // Преобразуем Route в Integer (distance)
                .filter(Objects::nonNull) // Фильтруем ненулевые расстояния
                .collect(Collectors.toSet());
    }



}
