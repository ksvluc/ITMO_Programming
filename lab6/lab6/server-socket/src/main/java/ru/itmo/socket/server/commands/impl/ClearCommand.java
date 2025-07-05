package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.server.commands.ServerCommand;
import ru.itmo.socket.server.manager.RouteTreeSetManager;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class ClearCommand implements ServerCommand {
    @Override
    public void execute(ObjectOutputStream oos, Object... args) throws IOException {
        RouteTreeSetManager.getInstance().clear();
        oos.writeUTF("Коллекция очищена.");
    }
}
