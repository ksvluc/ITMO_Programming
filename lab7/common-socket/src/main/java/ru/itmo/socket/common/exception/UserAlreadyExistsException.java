package ru.itmo.socket.common.exception;

/**
 * Выбрасывается, если при регистрации пользователь уже существует.
 * @author maxbarsukov
 */
public class UserAlreadyExistsException extends Exception {
  public UserAlreadyExistsException() {
    super();
  }
}
