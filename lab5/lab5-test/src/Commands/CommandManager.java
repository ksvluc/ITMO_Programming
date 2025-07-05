package Commands;

import Models.Route;
import java.util.LinkedList;

public class CommandManager {
    private final LinkedList<Route> routes;
    private final ExecuteScript executeScript;

    public CommandManager(LinkedList<Route> routes) {
        this.routes = routes;
        this.executeScript = new ExecuteScript(this); // Передаем текущий CommandManager
    }

    public void executeCommand(String userInput) {
        String[] parts = userInput.trim().split(" ", 2);
        String command = parts[0].toLowerCase();
        String argument = parts.length > 1 ? parts[1] : null;
            try {
                switch (command) {
                    case "execute_script" -> executeScript.execute(argument);
                    case "help" -> Help.execute();
                    case "info" -> Info.execute(routes);
                    case "show" -> Show.execute(routes);
                    case "add" -> Add.execute(routes);
                    case "update" -> Update.execute(routes, argument);
                    case "remove_by_id" -> RemoveById.execute(routes, argument);
                    case "clear" -> Clear.execute(routes);
                    case "save" -> Save.execute(routes);
                    case "exit" -> Exit.execute();
                    case "head" -> Head.execute(routes);
                    case "remove_head" -> RemoveHead.execute(routes);
                    case "remove_lower" -> RemoveLower.execute(routes, argument);
                    case "max_by_creation_date" -> MaxByCreationDate.execute(routes);
                    case "print_descending" -> PrintDescending.execute(routes);
                    case "print_unique_distance" -> PrintUniqueDistance.execute(routes);
                    default -> System.out.println("Неизвестная команда. Введите 'help' для списка команд.");
                }
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }
