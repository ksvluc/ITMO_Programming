package Commands;

import Models.Route;

import java.util.LinkedList;
import java.util.stream.Collectors;

public class PrintUniqueDistance {
    public static void execute(LinkedList<Route> routes) {
        if (routes.isEmpty()) {
            System.out.println("Коллекция пуста.");
            return;
        }

        System.out.println("Уникальные значения distance:");
        routes.stream()
                .map(Route::getDistance)
                .distinct()
                .forEach(System.out::println);
    }
}