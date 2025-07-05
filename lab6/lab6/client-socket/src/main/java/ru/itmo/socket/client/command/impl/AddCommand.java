package ru.itmo.socket.client.command.impl;

import ru.itmo.socket.client.command.ClientCommand;
import ru.itmo.socket.common.entity.Route;
import ru.itmo.socket.common.util.RouteInputHelper;

import java.util.Optional;
import java.util.Scanner;

public class AddCommand implements ClientCommand {

    @Override
    public Optional<Object> preProcess(Scanner scanner) {
        System.out.println("Введите данные нового элемента:");
        Route newRoute = RouteInputHelper.readRoute(scanner);
        return Optional.of(newRoute);
    }
}
