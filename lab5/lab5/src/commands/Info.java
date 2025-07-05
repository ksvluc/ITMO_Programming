package commands;

import utility.Console;
import managers.CollectionManager;
import utility.ExecutionResponse;

/**
 * Команда 'info'. Выводит информацию о коллекции.
 */
public class Info extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public Info(Console console, CollectionManager collectionManager) {
        super("info", "вывести информацию о коллекции");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResponse apply(String argument) {
        if (!argument.isEmpty()) {
            return new ExecutionResponse(false, "У команды 'info' не должно быть аргументов.");
        }
        String infoMessage = String.format("Тип коллекции: %s\nДата инициализации: %s\nДата последнего сохранения: %s\nКоличество элементов: %d",
                collectionManager.getCollection().getClass().getSimpleName(),
                collectionManager.getLastInitTime(),
                collectionManager.getLastSaveTime(),
                collectionManager.getCollection().size());
        return new ExecutionResponse(infoMessage);
    }
}
