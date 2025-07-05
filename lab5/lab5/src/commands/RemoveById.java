package commands;

import utility.Console;
import managers.CollectionManager;
import utility.ExecutionResponse;

/**
 * Команда 'remove_by_id'. Удаляет элемент из коллекции по его id.
 */
public class RemoveById extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public RemoveById(Console console, CollectionManager collectionManager) {
        super("remove_by_id id", "удалить элемент из коллекции по его id");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResponse apply(String argument) {
        if (argument.isEmpty()) {
            return new ExecutionResponse(false, "У команды 'remove_by_id' должен быть аргумент - id.");
        }
        long id;
        try {
            id = Long.parseLong(argument);
        } catch (NumberFormatException e) {
            return new ExecutionResponse(false, "ID должен быть числом.");
        }
        if (collectionManager.remove(id)) {
            return new ExecutionResponse("Элемент успешно удалён.");
        } else {
            return new ExecutionResponse(false, "Элемент с таким ID не найден.");
        }
    }
}
