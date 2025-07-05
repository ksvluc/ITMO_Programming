package ru.itmo.socket.client.command.impl;

import ru.itmo.socket.client.command.ClientCommand;

import java.util.Optional;
import java.util.Scanner;

public class MaxByCreationDateCommand implements ClientCommand {

    @Override
    public Optional<Object> preProcess(Scanner scanner) {
        // Эта команда не требует ввода параметров от пользователя
        return Optional.empty();
    }

}