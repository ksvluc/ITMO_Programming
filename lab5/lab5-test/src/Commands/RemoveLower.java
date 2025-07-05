package Commands;

import Models.Filler;
import Models.Route;
import java.util.LinkedList;

public class RemoveLower {
    public static void execute(LinkedList<Route> routes, String arg) {
        if (arg == null) {
            System.out.println("Ошибка: укажите элемент для сравнения.");
            return;
        }

        Route comparator = new Route();
        new Filler().fill(comparator);
        int initialSize = routes.size();
        routes.removeIf(route -> route.compareTo(comparator) < 0);
        System.out.println("Удалено элементов: " + (initialSize - routes.size()));
    }
}