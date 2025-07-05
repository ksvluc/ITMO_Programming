package commands;

import utility.Console;
import utility.ExecutionResponse;

/**
 * Команда 'exit'. Завершает программу (без сохранения в файл).
 */
public class Exit extends Command {
    private final Console console;

    public Exit(Console console) {
        super("exit", "завершить программу (без сохранения в файл)");
        this.console = console;
    }

    @Override
    public ExecutionResponse apply(String argument) {
        if (!argument.isEmpty()) {
            return new ExecutionResponse(false, "У команды 'exit' не должно быть аргументов.");
        }
        return new ExecutionResponse("exit");
    }
}
