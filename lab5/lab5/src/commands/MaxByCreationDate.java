package commands;

import utility.Console;
import managers.CollectionManager;
import utility.ExecutionResponse;
import models.Route;

import java.util.Comparator;

/**
 * Команда 'max_by_creation_date'. Выводит любой объект из коллекции, значение поля creationDate которого является максимальным.
 */
public class MaxByCreationDate extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public MaxByCreationDate(Console console, CollectionManager collectionManager) {
        super("max_by_creation_date", "вывести любой объект из коллекции, значение поля creationDate которого является максимальным");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResponse apply(String argument) {
        if (!argument.isEmpty()) {
            return new ExecutionResponse(false, "У команды 'max_by_creation_date' не должно быть аргументов.");
        }
        if (collectionManager.getCollection().isEmpty()) {
            return new ExecutionResponse("Коллекция пуста.");
        }
        Route maxRoute = collectionManager.getCollection().stream()
                .max(Comparator.comparing(Route::getCreationDate))
                .orElse(null);
        return new ExecutionResponse(maxRoute.toString());
    }
}
