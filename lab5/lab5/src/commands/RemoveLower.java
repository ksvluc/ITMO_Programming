package commands;

import utility.Console;
import managers.CollectionManager;
import utility.ExecutionResponse;
import models.Route;
import models.Ask;

/**
 * Команда 'remove_lower'. Удаляет из коллекции все элементы, меньшие, чем заданный.
 */
public class RemoveLower extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public RemoveLower(Console console, CollectionManager collectionManager) {
        super("remove_lower {element}", "удалить из коллекции все элементы, меньшие, чем заданный");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResponse apply(String argument) {
        try {
            if (!argument.isEmpty()) {
                return new ExecutionResponse(false, "Неправильное количество аргументов!\nИспользование: '" + getName() + "'");
            }
            console.println("* Удаление элементов, меньших чем заданный Route:");
            Route route = Ask.askRoute(console, 0);
            if (route == null || !route.validate()) {
                return new ExecutionResponse(false, "Поля route не валидны! Route не задан!");
            }
            collectionManager.getCollection().removeIf(r -> r.compareTo(route) < 0);
            return new ExecutionResponse("Элементы успешно удалены.");
        } catch (Ask.AskBreak e) {
            return new ExecutionResponse(false, "Отмена...");
        }
    }
}
