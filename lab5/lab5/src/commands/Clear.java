package commands;

import utility.Console;
import managers.CollectionManager;
import utility.ExecutionResponse;

/**
 * Команда 'clear'. Очищает коллекцию.
 */
public class Clear extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public Clear(Console console, CollectionManager collectionManager) {
        super("clear", "очистить коллекцию");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResponse apply(String argument) {
        if (!argument.isEmpty()) {
            return new ExecutionResponse(false, "У команды 'clear' не должно быть аргументов.");
        }
        collectionManager.getCollection().clear();
        return new ExecutionResponse("Коллекция успешно очищена.");
    }
}
