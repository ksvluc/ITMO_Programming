package Commands;

import Models.Route;

import java.util.LinkedList;

public class RemoveHead {
    public static void execute(LinkedList<Route> routes) {
        if (routes.isEmpty()) {
            System.out.println("Коллекция пуста.");
            return;
        }
        System.out.println("Удалён элемент: " + routes.removeFirst());
    }
}