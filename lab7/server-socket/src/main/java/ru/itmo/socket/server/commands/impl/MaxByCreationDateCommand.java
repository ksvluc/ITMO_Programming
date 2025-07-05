package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.common.entity.Route;
import ru.itmo.socket.server.commands.ServerCommand;
import ru.itmo.socket.server.manager.RouteTreeSetManager;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.NoSuchElementException;

public class MaxByCreationDateCommand implements ServerCommand {
    @Override
    public void execute(ObjectOutputStream oos, Object... args) throws IOException {
        try {
            // Получаем элемент с максимальной датой создания
            Route maxRoute = RouteTreeSetManager.getInstance().getElementWithMaxCreationDate();

            // Формируем ответ
            String response = "Элемент с максимальной датой создания:\n" +
                    maxRoute.toString();
            oos.writeUTF(response);

        } catch (NoSuchElementException e) {
            oos.writeUTF("Коллекция пуста, нет элементов для сравнения.");
        } catch (Exception e) {
            oos.writeUTF("Ошибка при поиске элемента с максимальной датой создания: " + e.getMessage());
        }
    }

    @Override
    public Class<?> getArgType() {
        return null;
    }
}