package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.server.commands.ServerCommand;
import ru.itmo.socket.server.commands.ScriptExecutor;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;


public class ExecuteScriptCommand implements ServerCommand {

    @Override
    public void execute(ObjectOutputStream oos, Object... args) throws IOException {
        String fileName = String.valueOf(args[0]);
//        oos.writeUTF("Имя файла скрипта: " + fileName);
        try {
            ScriptExecutor.execute(oos, fileName);
        } catch (URISyntaxException e) {
            oos.writeUTF("ошибка выполнения " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getNumberOfOutputLines(Object... args) {
        String fileName = String.valueOf(args[0]);
        return ScriptExecutor.countNumberOfCommands(fileName);
    }
}




