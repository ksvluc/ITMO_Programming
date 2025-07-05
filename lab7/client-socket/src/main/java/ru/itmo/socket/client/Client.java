package ru.itmo.socket.client;

import ru.itmo.socket.client.command.ClientCommand;
import ru.itmo.socket.client.command.ClientCommandContext;
import ru.itmo.socket.client.command.impl.ExitCommand;
import ru.itmo.socket.common.dto.CommandDto;
import ru.itmo.socket.common.exception.AppCommandNotFoundException;
import ru.itmo.socket.common.util.ConnectionContext;
import ru.itmo.socket.common.util.RouteInputHelper;
import ru.itmo.socket.common.entity.Route;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Optional;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws InterruptedException {
        while (true) {
            try {
                connectToServer();
                break; // finished with Exit
            } catch (ConnectException cE) {
                System.err.println("Server unreachable, waiting for server to start...");
                Thread.sleep(5_000);
            } catch (SocketException socketException) {
                System.err.println("Сервер отказал в подключении");
            } catch (Exception e) {
                System.err.println("Ошибка клиента: " + e.getMessage());
                e.printStackTrace();
                break;
            }
        }
    }

    private static void connectToServer() throws IOException {
        Scanner scanner = new Scanner(System.in);
        String host = ConnectionContext.getHost();
        int port = ConnectionContext.getPort();

        try (Socket socket = new Socket(host, port);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {

            System.out.println();
            System.out.println("Подключено к серверу " + host + ":" + port);

            while (true) {
                if (!processRemoteCommand(scanner, oos, ois)) {
                    break;
                }
            }

        }
    }

    /**
     * @return true - если любая команда кроме 'exit', false - если 'exit'
     */
    private static boolean processRemoteCommand(Scanner scanner, ObjectOutputStream oos, ObjectInputStream ois) throws IOException {
        boolean continueWork = true;

        try {
            System.out.println("\nВведите команду ");
            String stringCommand = scanner.nextLine().trim();

            ClientCommand clientCommand = ClientCommandContext.getCommand(stringCommand);
            Optional<Object> clientCommandParam = clientCommand.preProcess(scanner);
            Object lastSent = clientCommandParam.orElse(null);

            if (clientCommand instanceof ExitCommand) {
                continueWork = false;
            }

            CommandDto request = new CommandDto(stringCommand, lastSent);
            oos.writeObject(request);
            oos.flush();
            System.out.println("Отправлено серверу: " + request);

            // читаем первоначальный ответ
            int responseQuantity = Integer.parseInt(ois.readUTF());
            String lastResponse = null;
            for (int i = 0; i < responseQuantity; i++) {
                String response = ois.readUTF();
                lastResponse = response;
                System.out.println(response);
                if (response.contains("AppExit")) {
                    continueWork = false;
                }
            }

            // Обработка двухфазной команды update_by_id
            if (stringCommand.equalsIgnoreCase("update_by_id")
                    && "Готово к обновлению".equals(lastResponse)) {

                // спрошим остальные поля
                System.out.println("Теперь введите новые данные маршрута:");
                Route updatedRoute = RouteInputHelper.readRoute(scanner, true);
                updatedRoute.setId((Long) lastSent);

                // отправляем вторую часть запроса
                CommandDto secondRequest = new CommandDto(stringCommand, updatedRoute);
                oos.writeObject(secondRequest);
                oos.flush();
                System.out.println("Отправлено серверу: " + secondRequest);

                // читаем финальный ответ
                int respQty2 = Integer.parseInt(ois.readUTF());
                for (int i = 0; i < respQty2; i++) {
                    String resp = ois.readUTF();
                    System.out.println(resp);
                    if (resp.contains("AppExit")) continueWork = false;
                }
            }

        } catch (AppCommandNotFoundException e) {
            System.out.println("Команда не найдена");
        }

        return continueWork;
    }
}
