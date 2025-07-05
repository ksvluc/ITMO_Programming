package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.server.commands.ServerCommand;
import ru.itmo.socket.server.manager.RouteTreeSetManager;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Set;

public class PrintUniqueDistanceCommand implements ServerCommand {
    @Override
    public void execute(ObjectOutputStream oos, Object... args) throws IOException {
        // Получаем уникальные значения distance из менеджера
        Set<Integer> uniqueDistances = RouteTreeSetManager.getInstance().getUniqueDistances();

        if (uniqueDistances.isEmpty()) {
            oos.writeUTF("Нет маршрутов с уникальным расстоянием.");
        } else {
            StringBuilder answer = new StringBuilder("Уникальные значения расстояний:\n");
            for (Integer distance : uniqueDistances) {
                answer.append(distance).append("\n");
            }
            oos.writeUTF(answer.toString());
        }
    }
}