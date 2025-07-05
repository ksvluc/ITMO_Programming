package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.common.entity.Route;
import ru.itmo.socket.server.commands.ServerCommand;
import ru.itmo.socket.server.manager.RouteTreeSetManager;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class AddCommand implements ServerCommand {
    @Override
    public void execute(ObjectOutputStream oos, Object... args) throws IOException {
        Route newRoute = (Route) args[0];

        if (RouteTreeSetManager.getInstance().add(newRoute)) {
            oos.writeUTF("Элемент успешно добавлен.");
        } else {
            oos.writeUTF("Не удалось добавить элемент.");
        }
    }


    @Override
    public Class<?> getArgType() {
        return Route.class;
    }
}
