package commands;

import utility.Console;
import managers.CollectionManager;
import utility.ExecutionResponse;

/**
 * Команда 'save'. Сохраняет коллекцию в файл.
 */
public class Save extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public Save(Console console, CollectionManager collectionManager) {
        super("save", "сохранить коллекцию в файл");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResponse apply(String argument) {
        if (!argument.isEmpty()) {
            return new ExecutionResponse(false, "У команды 'save' не должно быть аргументов.");
        }
        collectionManager.saveCollection();
        return new ExecutionResponse("Коллекция успешно сохранена.");
    }
}
