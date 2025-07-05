package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.common.exception.AppExitException;
import ru.itmo.socket.server.commands.ServerCommand;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class ExitCommand implements ServerCommand {
    @Override
    public void execute(ObjectOutputStream oos, Object... args) throws IOException {
        oos.writeUTF("AppExit - Выход из программы...");
        throw new AppExitException();
    }
}
