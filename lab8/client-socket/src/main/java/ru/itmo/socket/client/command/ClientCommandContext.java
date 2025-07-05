package ru.itmo.socket.client.command;


import ru.itmo.socket.client.command.impl.*;
import ru.itmo.socket.common.command.AppCommand;

import java.util.HashMap;
import java.util.Map;

import static ru.itmo.socket.common.command.AppCommand.*;
import static ru.itmo.socket.common.command.AppCommand.LOGIN;

public class ClientCommandContext {

    private static final Map<AppCommand, ClientCommand> commandMap = initializeCommands();

    private static Map<AppCommand, ClientCommand> initializeCommands() {
        Map<AppCommand, ClientCommand> map = new HashMap<>();

        map.put(HELP, new DefaultCommand());
        map.put(INFO, new DefaultCommand());
        map.put(SHOW, new DefaultCommand());
        map.put(ADD, new AddCommand());
        map.put(UPDATE_ID, new UpdateByIdCommand());
        map.put(REMOVE_BY_ID, new RemoveByIdCommand());
        map.put(CLEAR, new DefaultCommand());
        map.put(EXECUTE_SCRIPT, new ExecuteScriptCommand());
        map.put(EXIT, new ExitCommand());
        map.put(REMOVE_HEAD, new HeadCommand());
        map.put(HISTORY, new DefaultCommand());
        map.put(MAX_BY_CREATION_DATE, new MaxByCreationDateCommand());
        map.put(PRINT_DESCENDING, new DefaultCommand());
        map.put(PRINT_UNIQUE_DISTANCE, new DefaultCommand());
        map.put(REGISTER, new RegisterCommand());
        map.put(LOGIN, new LoginCommand());
        return map;
    }

    public static ClientCommand getCommand(String commandName) {
        return commandMap.get(AppCommand.getByStringValue(commandName));
    }
}
