package Commands;

import Data.DataProvider;
import Models.Route;

import java.util.LinkedList;

public class Save {
    public static void execute(LinkedList<Route> routes) {
        try {
            new DataProvider().save(routes, "data.csv");
            System.out.println("Коллекция сохранена в файл.");
        } catch (Exception e) {
            System.out.println("Ошибка сохранения: " + e.getMessage());
        }
    }
}