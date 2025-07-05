package commands;

import utility.Console;
import managers.CollectionManager;
import utility.ExecutionResponse;
import models.Route;

import java.util.Comparator;

/**
 * Команда 'print_descending'. Выводит элементы коллекции в порядке убывания.
 */
public class PrintDescending extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public PrintDescending(Console console, CollectionManager collectionManager) {
        super("print_descending", "вывести элементы коллекции в порядке убывания");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResponse apply(String argument) {
        if (!argument.isEmpty()) {
            return new ExecutionResponse(false, "У команды 'print_descending' не должно быть аргументов.");
        }
        StringBuilder descendingList = new StringBuilder();
        collectionManager.getCollection().stream()
                .sorted(Comparator.reverseOrder())
                .forEach(route -> descendingList.append(route).append("\n"));
        return new ExecutionResponse(descendingList.toString().trim());
    }
}
