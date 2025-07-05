package ru.itmo.socket.server;

import ru.itmo.socket.common.util.ConnectionContext;
import ru.itmo.socket.server.concurrent.ProcessClientTask;
import ru.itmo.socket.server.db.DatabaseConfig;
import ru.itmo.socket.server.db.DatabaseInitializer;
import ru.itmo.socket.server.manager.RouteTreeSetManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;


//ssh -L 7777:localhost:5432 s467392@se.ifmo.ru -p 2222
public class Server {

    private static final AtomicInteger CONNECTION_COUNTER = new AtomicInteger(0);
    private static final ConcurrentSkipListSet<Integer> AVAILABLE_IDS = initAvailableIds();

    public static void main(String[] args) {
        initDb();
        startServer();
    }

    private static void initDb() {
        try (Connection connection = DatabaseConfig.getConnection()) {
            DatabaseInitializer.init(connection);
        } catch (SQLException e) {
            System.out.println("[Tech] [ERROR] ошибка инициализации базы");
            throw new RuntimeException(e);
        }
    }

    private static void startServer() {
        RouteTreeSetManager.getInstance().fetchInitialDataFromDb();

        int port = ConnectionContext.getPort();
        int maxConns = ConnectionContext.getMaxConnections();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Макс. количество подключений = " + maxConns);
            System.out.println("Сервер запущен и ожидает подключения...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                if (CONNECTION_COUNTER.get() >= maxConns) {
                    System.out.println("Максимальное число подключений достигнуто, отказ: " + clientSocket.getInetAddress());
                    clientSocket.close();
                    continue;
                }

                Integer connectionId = AVAILABLE_IDS.pollFirst();
                if (connectionId == null) {
                    System.out.println("Нет свободных ID для подключения: " + clientSocket.getInetAddress());
                    clientSocket.close();
                    continue;
                }

                CONNECTION_COUNTER.incrementAndGet();
                System.out.println("New connection #" + connectionId + " from " + clientSocket.getInetAddress());

                // передаем только connectionId, убираем пул и счётчик из ProcessClientTask
                Thread clientThread = new Thread(() -> {
                    try {
                        new ProcessClientTask(clientSocket, connectionId).run();
                    } catch (Exception e) {
                        System.err.println("Error processing client #" + connectionId + ": " + e.getMessage());
                        e.printStackTrace();
                    } finally {
                        CONNECTION_COUNTER.decrementAndGet();
                        AVAILABLE_IDS.add(connectionId);
                        System.out.println("Connection #" + connectionId + " closed. Active connections: "
                                + CONNECTION_COUNTER.get());
                    }
                }, "ClientThread-" + connectionId);

                clientThread.start();
            }

        } catch (IOException e) {
            System.err.println("Ошибка сервера: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static ConcurrentSkipListSet<Integer> initAvailableIds() {
        ConcurrentSkipListSet<Integer> set = new ConcurrentSkipListSet<>();
        for (int i = 1; i <= ConnectionContext.getMaxConnections(); i++) {
            set.add(i);
        }
        return set;
    }
}
