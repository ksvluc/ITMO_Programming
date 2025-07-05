package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.common.entity.Route;
import ru.itmo.socket.server.commands.ServerCommand;
import ru.itmo.socket.server.manager.RouteTreeSetManager;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class HeadCommand implements ServerCommand {
    @Override
    public void execute(ObjectOutputStream oos, Object... args) throws IOException {
        RouteTreeSetManager manager = RouteTreeSetManager.getInstance();

        if (manager.getAllElements().isEmpty()) {
            oos.writeUTF("Коллекция пуста, нечего извлекать");
            return;
        }

        // Получаем и удаляем первый элемент
        Route firstRoute = manager.head();

        // Формируем ответ
        String response = "Извлечен и удален первый маршрут:\n" + firstRoute;
        oos.writeUTF(response);
    }
}