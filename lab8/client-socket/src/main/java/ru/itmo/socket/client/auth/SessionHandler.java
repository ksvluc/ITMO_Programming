package ru.itmo.socket.client.auth;

import ru.itmo.socket.common.dto.UserDto;

public class SessionHandler {
  // сделано static, чтобы работать из любых статических методов
  private static UserDto currentUser;

  // язык оставляем без изменений
  public static String currentLanguage = "Русский";

  // возвращаем DTO, а не "User"
  public static UserDto getCurrentUser() {
    return currentUser;
  }

  // принимаем DTO, а не "User"
  public static void setCurrentUser(UserDto currentUser) {
    SessionHandler.currentUser = currentUser;
  }

  public static String getCurrentLanguage() {
    return currentLanguage;
  }

  public static void setCurrentLanguage(String currentLanguage) {
    SessionHandler.currentLanguage = currentLanguage;
  }
}
