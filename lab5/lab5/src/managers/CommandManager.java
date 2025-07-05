package managers;

import commands.Command;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    private final Map<String, Command> commands = new HashMap<>();

    public void register(String name, Command command) {
        commands.put(name, command);
    }

    public Command getCommand(String name) {
        return commands.get(name);
    }

    public Map<String, Command> getCommands() {
        return commands;
    }

    public void addToHistory(String commandName) {
        // Можно добавить логику для хранения истории команд
    }
}
