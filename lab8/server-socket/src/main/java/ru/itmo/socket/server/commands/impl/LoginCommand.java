package ru.itmo.socket.server.commands.impl;

import ru.itmo.socket.common.dto.UserDto;
import ru.itmo.socket.server.commands.ServerCommand;
import ru.itmo.socket.server.concurrent.UserContext;
import ru.itmo.socket.server.db.dao.UsersDao;
import ru.itmo.socket.server.db.exception.SqlRequestException;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class LoginCommand implements ServerCommand {

    UsersDao usersDao = new UsersDao();

    @Override
    public void execute(ObjectOutputStream oos, Object... args) throws IOException {
        UserDto userDto = (UserDto) args[0];

        if (UserContext.getAuthorized()) {
            oos.writeUTF("Пользователь уже авторизован login = " + UserContext.getLogin());
            return;
        }

        try {
            // про доп. ветку README (+ tg продублирую)
            // todo подключение к базе
            // todo про client intelliJ
            UserDto user = usersDao.findByUsername(userDto.getLogin());
            System.out.println("DEBUG LOGIN: Пользователь " + userDto.getLogin() + ", пароль из БД: '" + user.getPassword() + "', введенный пароль: '" + userDto.getPassword() + "'");
            System.out.println("DEBUG LOGIN: Длина пароля из БД: " + user.getPassword().length() + ", длина введенного пароля: " + userDto.getPassword().length());
            System.out.println("DEBUG LOGIN: Пароли равны: " + user.getPassword().equals(userDto.getPassword()));
            if (!user.getPassword().equals(userDto.getPassword())) {
                System.out.println("DEBUG LOGIN: Пароли НЕ совпадают, отправляем ошибку");
                oos.writeUTF("Неверный пароль пользователя");
                return;
            }
            System.out.println("DEBUG LOGIN: Пароли совпадают, авторизуем пользователя");
            UserContext.setLogin(userDto.getLogin());
            UserContext.setAuthorized(true);
            UserContext.setDbUserId(user.getId());
            System.out.println("DEBUG LOGIN: Успешно авторизован " + userDto.getLogin());
            oos.writeUTF(userDto.getLogin() + " успешно авторизован!");
        } catch (SqlRequestException e) {
            oos.writeUTF("user с таким login не найден");
        }
    }
}
