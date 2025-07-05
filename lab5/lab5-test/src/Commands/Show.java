package Commands;

import Models.Route;
import java.util.LinkedList;

public class Show {
    public static void execute(LinkedList<Route> routes) {
        if (routes.isEmpty()) {
            System.out.println("Коллекция пуста.");
            return;
        }
        routes.forEach(System.out::println);
    }
}