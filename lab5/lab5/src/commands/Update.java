package commands;

import utility.Console;
import managers.CollectionManager;
import utility.ExecutionResponse;
import models.Route;
import models.Ask;

/**
 * Команда 'update'. Обновляет значение элемента коллекции, id которого равен заданному.
 */
public class Update extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public Update(Console console, CollectionManager collectionManager) {
        super("update id {element}", "обновить значение элемента коллекции, id которого равен заданному");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionResponse apply(String argument) {
        try {
            if (argument.isEmpty()) {
                return new ExecutionResponse(false, "У команды 'update' должен быть аргумент - id.");
            }
            long id;
            try {
                id = Long.parseLong(argument);
            } catch (NumberFormatException e) {
                return new ExecutionResponse(false, "ID должен быть числом.");
            }
            Route oldRoute = collectionManager.byId(id);
            if (oldRoute == null) {
                return new ExecutionResponse(false, "Элемент с таким ID не найден.");
            }
            console.println("* Обновление Route с ID " + id + ":");
            Route newRoute = Ask.askRoute(console, id);
            if (newRoute != null && newRoute.validate()) {
                collectionManager.update(newRoute);
                return new ExecutionResponse("Route успешно обновлён!");
            } else {
                return new ExecutionResponse(false, "Поля route не валидны! Route не обновлён!");
            }
        } catch (Ask.AskBreak e) {
            return new ExecutionResponse(false, "Отмена...");
        }
    }
}
