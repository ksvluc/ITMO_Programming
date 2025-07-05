package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.common.entity.Route;
import ru.itmo.socket.server.commands.ServerCommand;
import ru.itmo.socket.server.manager.RouteTreeSetManager;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class UpdateByIdCommand implements ServerCommand {
    @Override
    public void execute(ObjectOutputStream oos, Object... args) throws IOException {
        // Фаза 1: получили Long → проверяем только существование
        if (args[0] instanceof Long) {
            long id = (Long) args[0];
            if (!RouteTreeSetManager.getInstance().existsById(id)) {
                oos.writeUTF("Элемент с id " + id + " не найден в вашей коллекции.");
            } else {
                oos.writeUTF("Готово к обновлению");
            }
            return;
        }

        // Фаза 2: получили full Route → обновляем
        Route updatedRoute = (Route) args[0];
        long id = updatedRoute.getId();
        boolean success = RouteTreeSetManager.getInstance().updateById(id, updatedRoute);
        oos.writeUTF(success
                ? "Элемент успешно обновлён."
                : "Ошибка обновления элемента с id " + id);
    }


    @Override
    public Class<?> getArgType() {
        return Route.class;
    }
}