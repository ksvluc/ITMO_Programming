package Commands;

import Models.Route;
import java.util.LinkedList;

public class RemoveById {
    public static void execute(LinkedList<Route> routes, String arg) {
        if (arg == null) {
            System.out.println("Ошибка: укажите ID.");
            return;
        }

        long id = Long.parseLong(arg);
        if (routes.removeIf(r -> r.getId() == id)) {
            System.out.println("Элемент удалён.");
        } else {
            System.out.println("Элемент с ID " + id + " не найден.");
        }
    }
}