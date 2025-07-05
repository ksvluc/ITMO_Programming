package ru.itmo.socket.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ru.itmo.socket.client.util.Localizator;
import ru.itmo.socket.common.entity.Coordinates;
import ru.itmo.socket.common.entity.Location;
import ru.itmo.socket.common.entity.Route;
import ru.itmo.socket.common.exception.InvalidFormException;

import java.time.LocalDateTime;

public class EditController {
  private Stage stage;
  private Route route;
  private Localizator localizator;

  // Основные поля Route
  @FXML private TextField nameField;
  @FXML private TextField coordXField;
  @FXML private TextField coordYField;
  @FXML private TextField distanceField;

  // Поля для Location from
  @FXML private TextField fromNameField;
  @FXML private TextField fromXField;
  @FXML private TextField fromYField;
  @FXML private TextField fromZField;
  @FXML private CheckBox fromCheckBox;

  // Поля для Location to
  @FXML private TextField toNameField;
  @FXML private TextField toXField;
  @FXML private TextField toYField;
  @FXML private TextField toZField;
  @FXML private CheckBox toCheckBox;

  @FXML private Button cancelButton;
  @FXML private Button okButton;

  @FXML
  void initialize() {
    // Валидация числовых полей
    coordXField.textProperty().addListener((obs, oldVal, newVal) -> {
      if (!newVal.matches("-?\\d*(\\.\\d*)?")) {
        coordXField.setText(oldVal);
      }
    });

    coordYField.textProperty().addListener((obs, oldVal, newVal) -> {
      if (!newVal.matches("-?\\d*")) {
        coordYField.setText(oldVal);
      }
    });

    distanceField.textProperty().addListener((obs, oldVal, newVal) -> {
      if (!newVal.matches("\\d*")) {
        distanceField.setText(oldVal);
      }
    });

    // Активация/деактивация полей Location в зависимости от CheckBox
    fromCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
      fromNameField.setDisable(!newVal);
      fromXField.setDisable(!newVal);
      fromYField.setDisable(!newVal);
      fromZField.setDisable(!newVal);
      if (!newVal) {
        fromNameField.clear();
        fromXField.clear();
        fromYField.clear();
        fromZField.clear();
      }
    });

    toCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
      toNameField.setDisable(!newVal);
      toXField.setDisable(!newVal);
      toYField.setDisable(!newVal);
      toZField.setDisable(!newVal);
      if (!newVal) {
        toNameField.clear();
        toXField.clear();
        toYField.clear();
        toZField.clear();
      }
    });

    cancelButton.setOnAction(event -> stage.close());
  }

  @FXML
  void ok() {
    try {
      validateForm();

      // Создаем объекты Location если CheckBox отмечены
      Location from = null;
      if (fromCheckBox.isSelected()) {
        from = new Location(
                fromNameField.getText().trim(),
                Integer.parseInt(fromXField.getText().trim()),
                Double.parseDouble(fromYField.getText().trim()),
                Integer.parseInt(fromZField.getText().trim())
        );
      }

      Location to = null;
      if (toCheckBox.isSelected()) {
        to = new Location(
                toNameField.getText().trim(),
                Integer.parseInt(toXField.getText().trim()),
                Double.parseDouble(toYField.getText().trim()),
                Integer.parseInt(toZField.getText().trim())
        );
      }

      // Создаем Route
      route = new Route(
              nameField.getText().trim(),
              new Coordinates(
                      Double.parseDouble(coordXField.getText().trim()),
                      Integer.parseInt(coordYField.getText().trim())
              ),
              from,
              to,
              Integer.parseInt(distanceField.getText().trim())
      );

      stage.close();
    } catch (InvalidFormException e) {
      showAlert(localizator.getKeyString("InvalidFormError"));
    } catch (NumberFormatException e) {
      showAlert(localizator.getKeyString("NumberFormatError"));
    } catch (IllegalArgumentException e) {
      showAlert(e.getMessage());
    }
  }

  private void validateForm() throws InvalidFormException {
    if (nameField.getText().trim().isEmpty()) {
      throw new InvalidFormException();
    }

    if (coordXField.getText().trim().isEmpty() || coordYField.getText().trim().isEmpty()) {
      throw new InvalidFormException();
    }

    if (distanceField.getText().trim().isEmpty() ||
            Integer.parseInt(distanceField.getText().trim()) <= 1) {
      throw new InvalidFormException();
    }

    // Проверка Location from если CheckBox отмечен
    if (fromCheckBox.isSelected()) {
      if (fromNameField.getText().trim().isEmpty() ||
              fromXField.getText().trim().isEmpty() ||
              fromYField.getText().trim().isEmpty() ||
              fromZField.getText().trim().isEmpty()) {
        throw new InvalidFormException();
      }
    }

    // Проверка Location to если CheckBox отмечен
    if (toCheckBox.isSelected()) {
      if (toNameField.getText().trim().isEmpty() ||
              toXField.getText().trim().isEmpty() ||
              toYField.getText().trim().isEmpty() ||
              toZField.getText().trim().isEmpty()) {
        throw new InvalidFormException();
      }
    }
  }

  private void showAlert(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(localizator.getKeyString("Error"));
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  public Route getRoute() {
    Route tempRoute = route;
    route = null;
    return tempRoute;
  }

  public void clear() {
    nameField.clear();
    coordXField.clear();
    coordYField.clear();
    distanceField.clear();

    fromCheckBox.setSelected(false);
    fromNameField.clear();
    fromXField.clear();
    fromYField.clear();
    fromZField.clear();

    toCheckBox.setSelected(false);
    toNameField.clear();
    toXField.clear();
    toYField.clear();
    toZField.clear();
  }

  public void fill(Route route) {
    this.route = route;

    nameField.setText(route.getName());
    coordXField.setText(String.valueOf(route.getCoordinates().getX()));
    coordYField.setText(String.valueOf(route.getCoordinates().getY()));
    distanceField.setText(String.valueOf(route.getDistance()));

    // Заполнение from location
    if (route.getFrom() != null) {
      fromCheckBox.setSelected(true);
      fromNameField.setText(route.getFrom().getName());
      fromXField.setText(String.valueOf(route.getFrom().getX()));
      fromYField.setText(String.valueOf(route.getFrom().getY()));
      fromZField.setText(String.valueOf(route.getFrom().getZ()));
    }

    // Заполнение to location
    if (route.getTo() != null) {
      toCheckBox.setSelected(true);
      toNameField.setText(route.getTo().getName());
      toXField.setText(String.valueOf(route.getTo().getX()));
      toYField.setText(String.valueOf(route.getTo().getY()));
      toZField.setText(String.valueOf(route.getTo().getZ()));
    }
  }

  public void changeLanguage() {
    // Обновляем тексты меток и кнопок
    cancelButton.setText(localizator.getKeyString("Cancel"));
    okButton.setText(localizator.getKeyString("OK"));

    // Здесь нужно добавить обновление других текстовых элементов интерфейса
    // в соответствии с вашими FXML-элементами
  }

  public void show() {
    if (!stage.isShowing()) {
      stage.showAndWait();
    }
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  public void setLocalizator(Localizator localizator) {
    this.localizator = localizator;
  }
}