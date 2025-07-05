package commands;

import utility.Console;
import utility.ExecutionResponse;

/**
 * Команда 'execute_script'. Считывает и исполняет скрипт из указанного файла.
 */
public class ExecuteScript extends Command {
    private final Console console;

    public ExecuteScript(Console console) {
        super("execute_script file_name", "считать и исполнить скрипт из указанного файла");
        this.console = console;
    }

    @Override
    public ExecutionResponse apply(String argument) {
        if (argument.isEmpty()) {
            return new ExecutionResponse(false, "У команды 'execute_script' должен быть аргумент - имя файла.");
        }
        // Здесь мы возвращаем успешность выполнения и сообщение
        return new ExecutionResponse(true, "execute_script " + argument);
    }
}
