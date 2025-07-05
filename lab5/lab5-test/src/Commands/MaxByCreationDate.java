package Commands;

import Models.Route;

import java.util.Comparator;
import java.util.LinkedList;

public class MaxByCreationDate {
    public static void execute(LinkedList<Route> routes) {
        if (routes.isEmpty()) {
            System.out.println("Коллекция пуста.");
            return;
        }

        Route maxRoute = routes.stream()
                .max(Comparator.comparing(Route::getCreationDate))
                .get();
        System.out.println(maxRoute);
    }
}