package ru.itmo.socket.client.command;

import java.util.Optional;
import java.util.Scanner;

public interface ClientCommand {
    default Optional<Object> preProcess(Scanner scanner) {
        return Optional.empty();
    }

}
