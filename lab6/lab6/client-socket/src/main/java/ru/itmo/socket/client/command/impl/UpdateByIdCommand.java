package ru.itmo.socket.client.command.impl;

import ru.itmo.socket.client.command.ClientCommand;
import ru.itmo.socket.common.entity.Route;
import ru.itmo.socket.common.util.RouteInputHelper;

import java.util.Optional;
import java.util.Scanner;

public class UpdateByIdCommand implements ClientCommand {

    @Override
    public Optional<Object> preProcess(Scanner scanner) {
        System.out.println("Введите данные элемента который хотите поменять:");
        Route newRoute = RouteInputHelper.readRoute(scanner, true);
        return Optional.of(newRoute);
    }
}
