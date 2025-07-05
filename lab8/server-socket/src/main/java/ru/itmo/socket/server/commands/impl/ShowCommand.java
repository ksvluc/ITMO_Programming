package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.server.commands.ServerCommand;
import ru.itmo.socket.server.manager.RouteTreeSetManager;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ShowCommand implements ServerCommand {
    @Override
    public void execute(ObjectOutputStream oos, Object... args) throws IOException {
        // Отправляем сериализованную коллекцию Route объектов
        var routes = new ArrayList<>(RouteTreeSetManager.getInstance().getAllElements());
        oos.writeObject(routes);
    }
}
