package ru.itmo.socket.client.command.impl;

import ru.itmo.socket.client.command.ClientCommand;
import ru.itmo.socket.common.util.RouteInputHelper;

import java.util.Optional;
import java.util.Scanner;

public class UpdateByIdCommand implements ClientCommand {
    @Override
    public Optional<Object> preProcess(Scanner scanner) {
        System.out.println("Введите id маршрута, который хотите обновить:");
        long id = RouteInputHelper.inputId(scanner);
        return Optional.of(id);
    }

}
