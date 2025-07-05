package commands;

import utility.Console;
import managers.CollectionManager;
import utility.ExecutionResponse;
import models.Route;

/**
 * Команда 'head'. Выводит первый элемент коллекции.
 */
public class Head extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public Head(Console console, CollectionManager collectionManager) {
        super("head", "вывести первый элемент коллекции");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResponse apply(String argument) {
        if (!argument.isEmpty()) {
            return new ExecutionResponse(false, "У команды 'head' не должно быть аргументов.");
        }
        if (collectionManager.getCollection().isEmpty()) {
            return new ExecutionResponse("Коллекция пуста.");
        }
        Route firstRoute = collectionManager.getCollection().getFirst();
        return new ExecutionResponse(firstRoute.toString());
    }
}
