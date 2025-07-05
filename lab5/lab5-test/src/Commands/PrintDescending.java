package Commands;

import Models.Route;
import java.util.Comparator;
import java.util.LinkedList;

public class PrintDescending {
    public static void execute(LinkedList<Route> routes) {
        if (routes.isEmpty()) {
            System.out.println("Коллекция пуста.");
            return;
        }

        routes.stream()
                .sorted(Comparator.reverseOrder())
                .forEach(System.out::println);
    }
}