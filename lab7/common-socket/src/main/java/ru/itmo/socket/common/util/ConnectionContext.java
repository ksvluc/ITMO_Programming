package ru.itmo.socket.common.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ConnectionContext {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 12345;
    private static final int MAX_CONNECTIONS = 2;

    // Храним логин и ID текущего пользователя
    private static String currentLogin;
    private static long currentUserId;

    public static int getPort() {
        return SERVER_PORT;
    }

    public static String getHost() {
        return SERVER_HOST;
    }

    public static int getMaxConnections() {
        return MAX_CONNECTIONS;
    }

    public static String getCurrentLogin() {
        return currentLogin;
    }

    public static void setCurrentLogin(String login) {
        currentLogin = login;
    }

    public static long getCurrentUserId() {
        return currentUserId;
    }

    public static void setCurrentUserId(long userId) {
        currentUserId = userId;
    }
}
