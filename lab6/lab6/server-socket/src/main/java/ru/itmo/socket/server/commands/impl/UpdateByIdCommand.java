package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.common.entity.Route;
import ru.itmo.socket.server.commands.ServerCommand;
import ru.itmo.socket.server.manager.RouteTreeSetManager;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class UpdateByIdCommand implements ServerCommand {
    @Override
    public void execute(ObjectOutputStream oos, Object... args) throws IOException {
        Route updatedRoute = (Route) args[0];
        long id = updatedRoute.getId();

        if (!RouteTreeSetManager.getInstance().containsId(id)) {
            oos.writeUTF("Элемент с id " + id + " не найден.");
            return;
        }

        // Получаем текущий элемент
        Route currentRoute = RouteTreeSetManager.getInstance().getAllElements().stream()
                .filter(rt -> rt.getId() == id)
                .findFirst()
                .orElse(null);

        if (RouteTreeSetManager.getInstance().updateById(id, updatedRoute)) {
            oos.writeUTF("Элемент успешно обновлён.");
        } else {
            oos.writeUTF("Ошибка обновления элемента с id " + id);
        }
    }

    @Override
    public Class<?> getArgType() {
        return Route.class;
    }
}



