package ru.itmo.socket.server.commands.impl;


import ru.itmo.socket.server.commands.ServerCommand;
import ru.itmo.socket.server.manager.RouteTreeSetManager;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class InfoCommand implements ServerCommand {
    @Override
    public void execute(ObjectOutputStream oos, Object... args) throws IOException {
        String info = RouteTreeSetManager.getInstance().getCollectionInfo();
        oos.writeUTF(info);
    }
}

