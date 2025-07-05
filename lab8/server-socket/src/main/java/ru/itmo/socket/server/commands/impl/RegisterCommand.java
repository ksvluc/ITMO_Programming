package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.common.dto.UserDto;
import ru.itmo.socket.server.commands.ServerCommand;
import ru.itmo.socket.server.db.dao.UsersDao;
import ru.itmo.socket.server.db.exception.SqlRequestException;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class RegisterCommand implements ServerCommand {
    UsersDao usersDao = new UsersDao();

    @Override
    public void execute(ObjectOutputStream oos, Object... args) throws IOException {
        UserDto userDto = (UserDto) args[0];
        try {
            UserDto registeredUser = usersDao.insert(userDto);
            String successMessage = userDto.getLogin() + " успешно зарегистрирован!";
            System.out.println("УСПЕХ: " + successMessage + " ID=" + registeredUser.getId());
            oos.writeUTF(successMessage);
        } catch (SqlRequestException e) {
            System.err.println("Ошибка регистрации пользователя " + userDto.getLogin() + ": " + e.getMessage());
            e.printStackTrace();
            if (e.getMessage().contains("duplicate key") || e.getMessage().contains("уже существует")) {
                oos.writeUTF("Пользователь с таким логином уже существует");
            } else {
                oos.writeUTF("Ошибка регистрации: " + e.getMessage());
            }
        }
    }
}
