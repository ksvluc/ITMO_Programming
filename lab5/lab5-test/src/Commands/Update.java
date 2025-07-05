package Commands;

import Models.Filler;
import Models.Route;
import java.util.LinkedList;

public class Update {
    public static void execute(LinkedList<Route> routes, String arg) {
        if (arg == null) {
            System.out.println("Ошибка: укажите ID.");
            return;
        }

        long id = Long.parseLong(arg);
        Route route = routes.stream()
                .filter(r -> r.getId() == id)
                .findFirst()
                .orElse(null);

        if (route == null) {
            System.out.println("Элемент с ID " + id + " не найден.");
            return;
        }

        new Filler().fill(route);
        System.out.println("Элемент обновлён.");
    }
}