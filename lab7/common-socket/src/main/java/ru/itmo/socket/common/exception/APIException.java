package ru.itmo.socket.common.exception;

/**
 * Выбрасывается, если в ответе сервера ошибка
 * @author maxbarsukov
 */
public class APIException extends Exception {
  public APIException(String message) {
    super(message);
  }
}
