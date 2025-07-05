package commands;

import utility.Console;
import managers.CollectionManager;
import utility.ExecutionResponse;

/**
 * Команда 'show'. Выводит все элементы коллекции в строковом представлении.
 */
public class Show extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public Show(Console console, CollectionManager collectionManager) {
        super("show", "вывести все элементы коллекции в строковом представлении");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResponse apply(String argument) {
        if (!argument.isEmpty()) {
            return new ExecutionResponse(false, "У команды 'show' не должно быть аргументов.");
        }
        return new ExecutionResponse(collectionManager.toString());
    }
}
