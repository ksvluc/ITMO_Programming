package commands;

import utility.Console;
import managers.CollectionManager;
import utility.ExecutionResponse;
import models.Route;

/**
 * Команда 'remove_head'. Выводит первый элемент коллекции и удаляет его.
 */
public class RemoveHead extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public RemoveHead(Console console, CollectionManager collectionManager) {
        super("remove_head", "вывести первый элемент коллекции и удалить его");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResponse apply(String argument) {
        if (!argument.isEmpty()) {
            return new ExecutionResponse(false, "У команды 'remove_head' не должно быть аргументов.");
        }
        if (collectionManager.getCollection().isEmpty()) {
            return new ExecutionResponse("Коллекция пуста.");
        }
        Route firstRoute = collectionManager.getCollection().removeFirst();
        return new ExecutionResponse(firstRoute.toString());
    }
}
