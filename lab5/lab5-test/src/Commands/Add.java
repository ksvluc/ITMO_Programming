package Commands;

import Models.Filler;
import Models.Route;
import java.util.LinkedList;

public class Add {
    public static void execute(LinkedList<Route> routes) {
        Route route = new Route();
        new Filler().fill(route);
        routes.add(route);
        System.out.println("Элемент добавлен. ID: " + route.getId());
    }
}