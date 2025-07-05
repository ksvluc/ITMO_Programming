package Commands;

import java.util.LinkedList;

public class Clear {
    public static void execute(LinkedList<?> routes) {
        routes.clear();
        System.out.println("Коллекция очищена.");
    }
}