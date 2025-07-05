package commands;

import managers.CommandManager;
import utility.Console;
import utility.ExecutionResponse;

/**
 * Команда 'help'. Выводит справку по доступным командам.
 */
public class Help extends Command {
    private final Console console;
    private final CommandManager commandManager;

    public Help(Console console, CommandManager commandManager) {
        super("help", "вывести справку по доступным командам");
        this.console = console;
        this.commandManager = commandManager;
    }

    @Override
    public ExecutionResponse apply(String argument) {
        if (!argument.isEmpty()) {
            return new ExecutionResponse(false, "У команды 'help' не должно быть аргументов.");
        }
        StringBuilder helpMessage = new StringBuilder("Доступные команды:\n");
        for (Command command : commandManager.getCommands().values()) {
            helpMessage.append(command.getName()).append(" : ").append(command.getDescription()).append("\n");
        }
        return new ExecutionResponse(helpMessage.toString().trim());
    }
}
