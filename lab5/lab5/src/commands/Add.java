package commands;

import utility.Console;
import managers.CollectionManager;
import utility.ExecutionResponse;
import models.Route;
import models.Ask;

/**
 * Команда 'add'. Добавляет новый элемент в коллекцию.
 */
public class Add extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public Add(Console console, CollectionManager collectionManager) {
        super("add {element}", "добавить новый элемент в коллекцию");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResponse apply(String argument) {
        try {
            if (!argument.isEmpty()) {
                return new ExecutionResponse(false, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
            }
            console.println("* Создание нового Route:");
            Route route = Ask.askRoute(console, collectionManager.getFreeId());
            if (route != null && route.validate()) {
                collectionManager.add(route);
                return new ExecutionResponse("Route успешно добавлен!");
            } else {
                return new ExecutionResponse(false, "Поля route не валидны! Route не создан!");
            }
        } catch (Ask.AskBreak e) {
            return new ExecutionResponse(false, "Отмена...");
        }
    }
}
