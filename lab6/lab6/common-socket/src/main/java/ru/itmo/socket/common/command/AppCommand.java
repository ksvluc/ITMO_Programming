package ru.itmo.socket.common.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.itmo.socket.common.exception.AppCommandNotFoundException;

import java.util.Arrays;

/**
 * Все доступные команды для приложения
 */
@AllArgsConstructor
@Getter
public enum AppCommand {
    HELP("help"),
    INFO("info"),
    SHOW("show"),
    ADD("add"),
    UPDATE_ID("update_id"),
    REMOVE_BY_ID("remove_by_id"),
    CLEAR("clear"),
    SAVE("save"),
    EXECUTE_SCRIPT("execute_script"),
    EXIT("exit"),
    HISTORY("history"),
    REMOVE_HEAD("head"),
    REMOVE_LOWER("remove_lower"),
    MAX_BY_CREATION_DATE("max_by_creation_date"),
    PRINT_DESCENDING("print_descending"),
    PRINT_UNIQUE_DISTANCE("print_unique_distance"),
    DISCONNECT_CLIENT("disconnect")
    ;

    private final String value;

    public static AppCommand getByStringValue(String commandName) {
        return Arrays.stream(AppCommand.values())
                .filter(c -> c.getValue().equalsIgnoreCase(commandName))
                .findFirst()
                .orElseThrow(() -> new AppCommandNotFoundException("command " + commandName + " not found"))
        ;
    }

}
