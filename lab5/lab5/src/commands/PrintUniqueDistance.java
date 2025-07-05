package commands;

import utility.Console;
import managers.CollectionManager;
import utility.ExecutionResponse;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Команда 'print_unique_distance'. Выводит уникальные значения поля distance всех элементов в коллекции.
 */
public class PrintUniqueDistance extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public PrintUniqueDistance(Console console, CollectionManager collectionManager) {
        super("print_unique_distance", "вывести уникальные значения поля distance всех элементов в коллекции");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResponse apply(String argument) {
        if (!argument.isEmpty()) {
            return new ExecutionResponse(false, "У команды 'print_unique_distance' не должно быть аргументов.");
        }
        Set<Integer> uniqueDistances = collectionManager.getCollection().stream()
                .map(route -> route.getDistance())
                .collect(Collectors.toSet());
        return new ExecutionResponse(uniqueDistances.toString());
    }
}
