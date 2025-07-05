package ru.itmo.socket.server.commands;

import java.io.IOException;
import java.io.ObjectOutputStream;

public interface ServerCommand {
    void execute(ObjectOutputStream oos, Object... args) throws IOException;

    default int getNumberOfOutputLines(Object... args){
        return 1;
    }


    default Class<?> getArgType() {
        return String.class;
    }
}