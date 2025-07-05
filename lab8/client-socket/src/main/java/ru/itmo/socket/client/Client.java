package ru.itmo.socket.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.itmo.socket.client.command.ClientCommand;
import ru.itmo.socket.client.command.ClientCommandContext;
import ru.itmo.socket.client.command.impl.ExitCommand;
import ru.itmo.socket.client.controllers.AuthController;
import ru.itmo.socket.client.controllers.EditController;
import ru.itmo.socket.client.controllers.MainController;
import ru.itmo.socket.common.dto.CommandDto;
import ru.itmo.socket.common.exception.AppCommandNotFoundException;
import ru.itmo.socket.common.util.ConnectionContext;
import ru.itmo.socket.common.util.RouteInputHelper;
import ru.itmo.socket.common.entity.Route;
import ru.itmo.socket.client.util.Localizator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Client extends Application {

    private static final int PORT = 23586;
    private Stage mainStage;
    private Localizator localizator;
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public static void main(String[] args) {
        // Проверяем аргументы для определения режима работы
        if (args.length > 0 && args[0].equals("--console")) {
            // Консольный режим
            runConsoleClient();
        } else {
            // GUI режим
            launch(args);
        }
    }

    private static void runConsoleClient() {
        while (true) {
            try {
                connectToServer();
                break; // завершаем после успешного выполнения
            } catch (ConnectException cE) {
                System.err.println("Server unreachable, waiting for server to start...");
                try {
                    Thread.sleep(5_000);
                } catch (InterruptedException ignored) {}
            } catch (SocketException socketException) {
                System.err.println("Сервер отказал в подключении");
            } catch (Exception e) {
                System.err.println("Ошибка клиента: " + e.getMessage());
                e.printStackTrace();
                break;
            }
        }
    }

    @Override
    public void start(Stage stage) {
        try {
            // Устанавливаем соединение при запуске GUI
            socket = new Socket(ConnectionContext.getHost(), ConnectionContext.getPort());
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());

            System.out.println("Connected to server: " +
                    socket.getInetAddress() + ":" + socket.getPort());
        } catch (IOException e) {
            System.err.println("Failed to connect to server: " + e.getMessage());
            System.exit(1);
        }

        localizator = new Localizator(
                ResourceBundle.getBundle("locales/gui", new Locale("ru", "RU"))
        );
        mainStage = stage;
        authStage();
    }

    @Override
    public void stop() {
        // Закрываем соединение при выходе из приложения
        try {
            if (ois != null) ois.close();
            if (oos != null) oos.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }

    public void startMain() {
        try {
            var mainLoader = new FXMLLoader(getClass().getResource("/main.fxml"));
            var mainRoot = loadFxml(mainLoader);

            var editLoader = new FXMLLoader(getClass().getResource("/edit.fxml"));
            var editRoot = loadFxml(editLoader);

            var editScene = new Scene(editRoot);
            var editStage = new Stage();
            editStage.setScene(editScene);
            editStage.setResizable(false);
            editStage.setTitle("Routes");
            EditController editController = editLoader.getController();

            editController.setStage(editStage);
            editController.setLocalizator(localizator);

            MainController mainController = mainLoader.getController();
            mainController.setEditController(editController);

            // Передаем сокет и потоки в MainController
            mainController.setContext(socket, ois, oos, localizator, mainStage);
            mainController.setAuthCallback(this::authStage);

            mainStage.setScene(new Scene(mainRoot));
            mainController.setRefreshing(true);
            mainController.refresh();
            mainStage.show();
        } catch (Exception e) {
            System.err.println("Failed to start main window: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void authStage() {
        try {
            var authLoader = new FXMLLoader(getClass().getResource("/auth.fxml"));
            Parent authRoot = loadFxml(authLoader);
            AuthController authController = authLoader.getController();

            // Передаем сокет и потоки в AuthController
            authController.setSocket(socket);
            authController.setOis(ois);
            authController.setOos(oos);

            authController.setCallback(this::startMain);
            authController.setLocalizator(localizator);

            mainStage.setScene(new Scene(authRoot));
            mainStage.setTitle("Authentication");
            mainStage.setResizable(false);
            mainStage.show();
        } catch (Exception e) {
            System.err.println("Failed to start auth window: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Parent loadFxml(FXMLLoader loader) {
        try {
            return loader.load();
        } catch (IOException e) {
            System.err.println("Can't load FXML: " + loader.getLocation());
            e.printStackTrace();
            System.exit(1);
            return null;
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

    private static boolean processRemoteCommand(
            Scanner scanner,
            ObjectOutputStream oos,
            ObjectInputStream ois
    ) throws IOException {
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

            // Читаем количество строк ответа
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

                // Запрашиваем данные маршрута
                System.out.println("Теперь введите новые данные маршрута:");
                Route updatedRoute = RouteInputHelper.readRoute(scanner, true);
                updatedRoute.setId((Long) lastSent);

                // Отправляем обновленный маршрут
                CommandDto secondRequest = new CommandDto(stringCommand, updatedRoute);
                oos.writeObject(secondRequest);
                oos.flush();
                System.out.println("Отправлено серверу: " + secondRequest);

                // Читаем финальный ответ
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