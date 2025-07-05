package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.server.commands.ServerCommand;
import ru.itmo.socket.server.manager.RouteTreeSetManager;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class ExitCommand implements ServerCommand {
    private static final String DEFAULT_SAVE_FILE = "server-socket/src/main/resources/collection.txt";

    @Override
    public void execute(ObjectOutputStream oos, Object... args) throws IOException {
        try {
            // Получаем имя файла (если не передано, используем значение по умолчанию)
            String fileName = (args != null && args.length > 0 && args[0] != null)
                    ? args[0].toString()
                    : DEFAULT_SAVE_FILE;

            // Сохраняем коллекцию
            RouteTreeSetManager manager = RouteTreeSetManager.getInstance();
            if (!manager.getAllElements().isEmpty()) {
                new SaveCommand().execute(oos, fileName);
            }

            oos.writeUTF("AppExit - Коллекция сохранена в " + fileName + ". Выход из программы...");
        } catch (Exception e) {
            oos.writeUTF("AppExit - Ошибка при сохранении коллекции: " + e.getMessage());
        }
    }
}