import Data.DataProvider;
import Models.Route;
import Commands.CommandManager;
import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    private static final String DEFAULT_FILENAME = "data.csv";

    public static void main(String[] args) {
        String filename = (args.length > 0) ? args[0] : DEFAULT_FILENAME;

        LinkedList<Route> routes = loadRoutes(filename);
        CommandManager commandManager = new CommandManager(routes);

        startInteractiveMode(commandManager);
    }

    private static LinkedList<Route> loadRoutes(String filename) {
        DataProvider dataProvider = new DataProvider();
        File file = new File(filename);

        if (!file.exists()) {
            System.out.println("Файл " + filename + " не найден. Создана новая коллекция.");
            return new LinkedList<>();
        }

        try {
            LinkedList<Route> routes = dataProvider.load(filename);
            System.out.println("Загружено " + routes.size() + " маршрутов из " + filename);
            return routes;
        } catch (IOException e) {
            System.err.println("Ошибка загрузки файла:");
            e.printStackTrace();
            return new LinkedList<>();
        }
    }

    private static void startInteractiveMode(CommandManager commandManager) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Программа управления коллекцией маршрутов");
        System.out.println("Введите 'help' для списка команд");

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Выход из программы");
                break;
            }

            if (!input.isEmpty()) {
                commandManager.executeCommand(input);
            }
        }
        scanner.close();
    }
}