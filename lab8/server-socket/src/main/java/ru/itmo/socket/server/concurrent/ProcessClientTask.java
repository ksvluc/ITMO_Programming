package ru.itmo.socket.server.concurrent;

import ru.itmo.socket.common.dto.CommandDto;
import ru.itmo.socket.common.exception.AppExitException;
import ru.itmo.socket.server.commands.ServerCommand;
import ru.itmo.socket.server.commands.ServerCommandContext;
import ru.itmo.socket.server.commands.impl.ExitCommand;
import ru.itmo.socket.server.commands.impl.LoginCommand;
import ru.itmo.socket.server.commands.impl.RegisterCommand;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;

/**
 * Runnable для обработки одного клиента.
 * Теперь получает connectionId единожды из Server.
 */
public class ProcessClientTask implements Runnable {
    private final Socket clientSocket;
    private final int connectionId;

    public ProcessClientTask(Socket clientSocket, int connectionId) {
        this.clientSocket = clientSocket;
        this.connectionId = connectionId;
    }

    @Override
    public void run() {
        try {
            UserContext.initUserContext(connectionId);
            DbUserContext.connectToDb();
            System.out.printf("[Tech] [INFO] Клиент #%d вошёл в систему%n", connectionId);
            processConnection();
        } catch (SQLException e) {
            System.err.println("Ошибка подключения к БД для клиента #" + connectionId + ": " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Ошибка обработки клиентa #" + connectionId + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                DbUserContext.disconnect();
            } catch (SQLException e) {
                System.err.println("Ошибка закрытия подключения к БД для клиента #" + connectionId);
            }
            UserContext.destroyUserContext();
            System.out.printf("[Tech] [INFO] Клиент #%d отключился%n", connectionId);
        }
    }

    private void processConnection() throws IOException, ClassNotFoundException {
        try (ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
             ObjectInputStream  ois = new ObjectInputStream(clientSocket.getInputStream())) {

            while (true) {
                CommandDto dto = (CommandDto) ois.readObject();
                System.out.printf("Получено от клиента #%d: %s%n", connectionId, dto);

                ServerCommand cmd = ServerCommandContext.getCommand(dto.getCommandName());
                if (cmd == null) {
                    oos.writeUTF("Команда не найдена");
                    oos.flush();
                    continue;
                }

                int lines = cmd.getNumberOfOutputLines(dto.getArg());
                oos.writeUTF(String.valueOf(lines));

                // авторизация
                if (!UserContext.getAuthorized()
                        && !(cmd instanceof LoginCommand
                        || cmd instanceof RegisterCommand
                        || cmd instanceof ExitCommand)) {
                    oos.writeUTF("Неавторизованный пользователь не может выполнять ничего кроме login/register/exit");
                    oos.flush();
                    continue;
                }

                try {
                    cmd.execute(oos, dto.getArg());
                } catch (AppExitException exit) {
                    break;
                }

                oos.flush();
            }
        } catch (IOException e) {
            System.err.printf("Соединение с клиентом #%d разорвано%n", connectionId);
        }
    }
}
