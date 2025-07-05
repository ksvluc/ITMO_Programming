package ru.itmo.socket.client.controllers;

import lombok.Getter;
import lombok.Setter;


import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ru.itmo.socket.client.auth.SessionHandler;
import ru.itmo.socket.client.ui.DialogManager;
import ru.itmo.socket.client.util.Localizator;
import ru.itmo.socket.common.dto.CommandDto;
import ru.itmo.socket.common.dto.UserDto;
import ru.itmo.socket.common.exception.InvalidFormException;
import ru.itmo.socket.common.exception.APIException;
import ru.itmo.socket.common.exception.UserAlreadyExistsException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

public class AuthController {
    // сеттеры для DI
    @Setter
    private Runnable callback;
  @Setter
  private Localizator localizator;
  @Setter
  private Socket socket;
  @Setter
  private ObjectOutputStream oos;
  @Setter
  private ObjectInputStream ois;

  private final HashMap<String, Locale> localeMap = new HashMap<>() {{
    put("Русский", new Locale("ru", "RU"));
    put("English(IN)", new Locale("en", "IN"));
    put("Íslenska", new Locale("is", "IS"));
    put("Svenska", new Locale("sv", "SE"));
  }};

  @FXML private Label titleLabel;
  @FXML private TextField loginField;
  @FXML private PasswordField passwordField;
  @FXML private Button okButton;
  @FXML private CheckBox signUpButton;
  @FXML private ComboBox<String> languageComboBox;

  @FXML
  void initialize() {
    // Инициализация соединения
    if (localizator == null) {
      ResourceBundle bundle = ResourceBundle.getBundle("locales/gui", new Locale("ru", "RU"));
      this.localizator = new Localizator(bundle);
    }
    changeLanguage(); // Теперь вызов безопасен
    
    // языки
    languageComboBox.setItems(FXCollections.observableArrayList(localeMap.keySet()));
    languageComboBox.setValue(SessionHandler.getCurrentLanguage());
    languageComboBox.getSelectionModel().selectedItemProperty().addListener((obs, o, nv) -> {
      localizator.setBundle(
              ResourceBundle.getBundle("locales/gui", localeMap.get(nv))
      );
      SessionHandler.setCurrentLanguage(nv);
      changeLanguage();
    });

    // валидация полей
    loginField.textProperty().addListener((obs, o, v) -> {
      if (v.length() > 40) loginField.setText(o);
    });
    passwordField.textProperty().addListener((obs, o, v) -> {
      if (v.contains(" ")) passwordField.setText(o);
    });

    changeLanguage();
  }

  @FXML
  void ok() {
    if (signUpButton.isSelected()) register();
    else authenticate();
  }

  private void register() {
    try {
      validateForm();
      UserDto user = new UserDto(loginField.getText(), passwordField.getText());

      // Формируем команду для сервера
      CommandDto command = new CommandDto("register", user);
      sendCommand(command);

      // Обрабатываем ответ
      int responseCount = Integer.parseInt(ois.readUTF());
      StringBuilder responseBuilder = new StringBuilder();
      for (int i = 0; i < responseCount; i++) {
        responseBuilder.append(ois.readUTF());
        if (i < responseCount - 1) responseBuilder.append("\n");
      }
      String response = responseBuilder.toString();
      System.out.println("CLIENT DEBUG: Получен ответ от сервера: '" + response + "'");

      // Проверяем успех по началу сообщения с логином пользователя
      if (response.startsWith(user.getLogin() + " ") && !response.contains("уже существует")) {
        System.out.println("CLIENT DEBUG: Успешная регистрация");
        SessionHandler.setCurrentUser(user);
        DialogManager.info("RegisterSuccess", localizator);
        callback.run();
      } else {
        System.out.println("CLIENT DEBUG: Ошибка регистрации");
        handleErrorResponse(response);
      }
    } catch (UserAlreadyExistsException e) {
      DialogManager.alert("UserAlreadyExists", localizator);
    } catch (IOException e) {
      DialogManager.alert("UnavailableError", localizator);
    } catch (APIException e) {
      if (e.getMessage() != null && e.getMessage().contains("уже существует")) {
        DialogManager.alert("UserAlreadyExists", localizator);
      } else {
        DialogManager.alert("RegisterError", localizator);
      }
    } catch (InvalidFormException e) {
        DialogManager.alert("InvalidCredentials", localizator);
    }
  }

  private void authenticate() {
    try {
      validateForm();
      UserDto user = new UserDto(loginField.getText(), passwordField.getText());

      // Формируем команду для сервера
      CommandDto command = new CommandDto("login", user);
      sendCommand(command);

      // Обрабатываем ответ
      int responseCount = Integer.parseInt(ois.readUTF());
      StringBuilder responseBuilder = new StringBuilder();
      for (int i = 0; i < responseCount; i++) {
        responseBuilder.append(ois.readUTF());
        if (i < responseCount - 1) responseBuilder.append("\n");
      }
      String response = responseBuilder.toString();
      System.out.println("CLIENT DEBUG: Получен ответ авторизации: '" + response + "'");

      // Проверяем успешную авторизацию по началу сообщения с логином и слову "успешно"
      if (response.startsWith(user.getLogin() + " ") && response.contains("успешно")) {
        System.out.println("CLIENT DEBUG: Успешная авторизация");
        SessionHandler.setCurrentUser(user);
        callback.run();
      } else {
        System.out.println("CLIENT DEBUG: Ошибка авторизации: " + response);
        throw new APIException(response);
      }
    } catch (IOException e) {
      DialogManager.alert("UnavailableError", localizator);
    } catch (APIException | InvalidFormException e) {
      DialogManager.alert("SignInError", localizator);
    }
  }

  private void sendCommand(CommandDto command) throws IOException {
    oos.writeObject(command);
    oos.flush();
  }

  private void handleErrorResponse(String response) throws APIException, UserAlreadyExistsException {
    if (response.contains("уже существует")) {
      throw new UserAlreadyExistsException();
    } else {
      throw new APIException(response);
    }
  }

  private void validateForm() throws InvalidFormException {
    String login = loginField.getText().trim();
    String pass  = passwordField.getText().trim();
    if (login.isEmpty() || login.length() > 40 || pass.isEmpty()) {
      throw new InvalidFormException();
    }
  }

  public void changeLanguage() {
    ResourceBundle b = localizator.getBundle();
    titleLabel.setText(b.getString("AuthTitle"));
    loginField.setPromptText(b.getString("LoginField"));
    passwordField.setPromptText(b.getString("PasswordField"));
    signUpButton.setText(b.getString("SignUpButton"));
    okButton.setText(
            signUpButton.isSelected()
                    ? b.getString("RegisterButton")
                    : b.getString("SignInButton")
    );
  }

    public void closeConnection() {
    try {
      if (ois != null) ois.close();
      if (oos != null) oos.close();
      if (socket != null) socket.close();
    } catch (IOException e) {
      System.err.println("Error closing connection: " + e.getMessage());
    }
  }

}