package Commands;

import Models.Route;
import java.util.LinkedList;

public class Info {
    public static void execute(LinkedList<Route> routes) {
        System.out.printf("""
            Тип коллекции: %s
            Количество элементов: %d
            Дата инициализации: %s
            """,
                routes.getClass().getName(),
                routes.size(),
                routes.isEmpty() ? "не инициализирована" : routes.getFirst().getCreationDate()
        );
    }
}