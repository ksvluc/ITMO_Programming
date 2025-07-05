package ru.itmo.socket.client.controllers;

import javafx.animation.FillTransition;
import javafx.animation.PathTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import ru.itmo.socket.client.auth.SessionHandler;
import ru.itmo.socket.client.script.ScriptExecutor;
import ru.itmo.socket.client.ui.DialogManager;
import ru.itmo.socket.client.util.Localizator;
import ru.itmo.socket.client.util.RoutePresenter;
import ru.itmo.socket.common.dto.CommandDto;
import ru.itmo.socket.common.entity.Route;
import ru.itmo.socket.common.exception.APIException;
import ru.itmo.socket.common.exception.BadOwnerException;
import ru.itmo.socket.common.exception.NotFoundException;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class MainController {
  private Localizator localizator;
  private Stage stage;
  private ObjectInputStream ois;
  private ObjectOutputStream oos;
  private Socket socket;

  private Runnable authCallback;
  private volatile boolean isRefreshing = false;
  private List<Route> collection;
  private EditController editController;

  private final HashMap<String, Locale> localeMap = new HashMap<>() {{
    put("Русский", new Locale("ru", "RU"));
    put("English(IN)", new Locale("en", "IN"));
    put("Íslenska", new Locale("is", "IS"));
    put("Svenska", new Locale("sv", "SE"));
  }};

  private HashMap<String, Color> colorMap;
  private HashMap<Long, Label> infoMap;
  private Random random;

  @FXML private ComboBox<String> languageComboBox;
  @FXML private Label userLabel;

  @FXML private Button helpButton;
  @FXML private Button infoButton;
  @FXML private Button addButton;
  @FXML private Button updateButton;
  @FXML private Button removeByIdButton;
  @FXML private Button clearButton;
  @FXML private Button executeScriptButton;
  @FXML private Button headButton;
  @FXML private Button addIfMaxButton;
  @FXML private Button addIfMinButton;
  @FXML private Button sumOfDistanceButton;
  @FXML private Button filterByDistanceButton;
  @FXML private Button filterContainsNameButton;
  @FXML private Button exitButton;
  @FXML private Button logoutButton;

  @FXML private Tab tableTab;
  @FXML private TableView<Route> tableTable;

  @FXML private TableColumn<Route, Long> ownerColumn;
  @FXML private TableColumn<Route, Long> idColumn;
  @FXML private TableColumn<Route, String> nameColumn;
  @FXML private TableColumn<Route, Double> xColumn;
  @FXML private TableColumn<Route, Integer> yColumn;
  @FXML private TableColumn<Route, String> dateColumn;
  @FXML private TableColumn<Route, String> fromColumn;
  @FXML private TableColumn<Route, String> toColumn;
  @FXML private TableColumn<Route, Integer> distanceColumn;

  @FXML private Tab visualTab;
  @FXML private AnchorPane visualPane;

  @FXML
  public void initialize() {
    colorMap = new HashMap<>();
    infoMap = new HashMap<>();
    random = new Random();

    languageComboBox.setItems(FXCollections.observableArrayList(localeMap.keySet()));
    languageComboBox.setStyle("-fx-font: 13px \"Sergoe UI\";");
    languageComboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
      localizator.setBundle(ResourceBundle.getBundle("locales/gui", localeMap.get(newValue)));
      changeLanguage();
    });

    // Настройка колонок таблицы
    ownerColumn.setCellValueFactory(cell -> new SimpleLongProperty(cell.getValue().getCreatorId()).asObject());
    idColumn.setCellValueFactory(cell -> new SimpleLongProperty(cell.getValue().getId()).asObject());
    nameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));
    xColumn.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().getCoordinates().getX()).asObject());
    yColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getCoordinates().getY()).asObject());
    dateColumn.setCellValueFactory(cell -> new SimpleStringProperty(localizator.getDate(cell.getValue().getCreationDate())));
    fromColumn.setCellValueFactory(cell -> new SimpleStringProperty(
            cell.getValue().getFrom() != null ? cell.getValue().getFrom().getName() : "null"));
    toColumn.setCellValueFactory(cell -> new SimpleStringProperty(
            cell.getValue().getTo() != null ? cell.getValue().getTo().getName() : "null"));
    distanceColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getDistance()).asObject());

    tableTable.setRowFactory(tableView -> {
      var row = new TableRow<Route>();
      row.setOnMouseClicked(mouseEvent -> {
        if (mouseEvent.getClickCount() == 2 && !row.isEmpty()) {
          doubleClickUpdate(row.getItem());
        }
      });
      return row;
    });

    visualTab.setOnSelectionChanged(event -> visualise(false));
  }

  @FXML
  public void exit() {
    System.exit(0);
  }

  @FXML
  public void logout() {
    SessionHandler.setCurrentUser(null);
    SessionHandler.setCurrentLanguage("Русский");
    setRefreshing(false);
    authCallback.run();
  }

  @FXML
  public void help() {
    try {
      sendCommand(new CommandDto("help", null));
      int responseCount = Integer.parseInt(ois.readUTF());
      StringBuilder response = new StringBuilder();
      for (int i = 0; i < responseCount; i++) {
        response.append(ois.readUTF());
        if (i < responseCount - 1) response.append("\n");
      }

      DialogManager.createAlert(localizator.getKeyString("Help"), response.toString(), Alert.AlertType.INFORMATION, true);
    } catch (IOException e) {
      DialogManager.alert("UnavailableError", localizator);
    }
  }

  @FXML
  public void info() {
    try {
      sendCommand(new CommandDto("info", null));
      int responseCount = Integer.parseInt(ois.readUTF());
      StringBuilder response = new StringBuilder();
      for (int i = 0; i < responseCount; i++) {
        response.append(ois.readUTF());
        if (i < responseCount - 1) response.append("\n");
      }

      DialogManager.createAlert(localizator.getKeyString("Info"), response.toString(), Alert.AlertType.INFORMATION, true);
    } catch (IOException e) {
      DialogManager.alert("UnavailableError", localizator);
    }
  }

  @FXML
  public void add() {
    editController.clear();
    editController.show();
    Route route = editController.getRoute();
    if (route != null) {
      try {
        sendCommand(new CommandDto("add", route));
        int responseCount = Integer.parseInt(ois.readUTF());
        StringBuilder response = new StringBuilder();
        for (int i = 0; i < responseCount; i++) {
          response.append(ois.readUTF());
          if (i < responseCount - 1) response.append("\n");
        }

        loadCollection();
        DialogManager.createAlert(localizator.getKeyString("Add"), response.toString(), Alert.AlertType.INFORMATION, false);
      } catch (IOException e) {
        DialogManager.alert("UnavailableError", localizator);
      }
    }
  }

  @FXML
  public void update() {
    Optional<String> input = DialogManager.createDialog(localizator.getKeyString("Update"), "ID:");
    if (input.isPresent() && !input.get().equals("")) {
      try {
        long id = Long.parseLong(input.orElse(""));
        Route route = collection.stream()
                .filter(r -> r.getId() == id)
                .findAny()
                .orElse(null);
        if (route == null) throw new NotFoundException();
        if (route.getCreatorId() != SessionHandler.getCurrentUser().getId()) throw new BadOwnerException();
        doubleClickUpdate(route, false);
      } catch (NumberFormatException e) {
        DialogManager.alert("NumberFormatException", localizator);
      } catch (BadOwnerException e) {
        DialogManager.alert("BadOwnerError", localizator);
      } catch (NotFoundException e) {
        DialogManager.alert("NotFoundException", localizator);
      }
    }
  }

  @FXML
  public void removeById() {
    Optional<String> input = DialogManager.createDialog(localizator.getKeyString("RemoveByID"), "ID: ");
    if (input.isPresent() && !input.get().equals("")) {
      try {
        long id = Long.parseLong(input.orElse(""));
        Route route = collection.stream()
                .filter(r -> r.getId() == id)
                .findAny()
                .orElse(null);
        if (route == null) throw new NotFoundException();
        if (route.getCreatorId() != SessionHandler.getCurrentUser().getId()) throw new BadOwnerException();

        sendCommand(new CommandDto("remove_by_id", id));
        int responseCount = Integer.parseInt(ois.readUTF());
        StringBuilder response = new StringBuilder();
        for (int i = 0; i < responseCount; i++) {
          response.append(ois.readUTF());
          if (i < responseCount - 1) response.append("\n");
        }

        loadCollection();
        DialogManager.createAlert(
                localizator.getKeyString("RemoveByID"), response.toString(), Alert.AlertType.INFORMATION, false
        );
      } catch (IOException e) {
        DialogManager.alert("UnavailableError", localizator);
      } catch (NumberFormatException e) {
        DialogManager.alert("NumberFormatException", localizator);
      } catch (BadOwnerException e) {
        DialogManager.alert("BadOwnerError", localizator);
      } catch (NotFoundException e) {
        DialogManager.alert("NotFoundException", localizator);
      }
    }
  }

  @FXML
  public void clear() {
    try {
      sendCommand(new CommandDto("clear", null));
      int responseCount = Integer.parseInt(ois.readUTF());
      StringBuilder response = new StringBuilder();
      for (int i = 0; i < responseCount; i++) {
        response.append(ois.readUTF());
        if (i < responseCount - 1) response.append("\n");
      }

      loadCollection();
      DialogManager.createAlert(localizator.getKeyString("Clear"), response.toString(), Alert.AlertType.INFORMATION, false);
    } catch (IOException e) {
      DialogManager.alert("UnavailableError", localizator);
    }
  }

  @FXML
  public void executeScript() {
    var chooser = new FileChooser();
    chooser.setInitialDirectory(new File("."));
    var file = chooser.showOpenDialog(stage);
    if (file != null) {
      var result = (new ScriptExecutor(this, localizator)).run(file.getAbsolutePath());
      if (result == ScriptExecutor.ExitCode.ERROR) {
        DialogManager.alert("ScriptExecutionErr", localizator);
      } else {
        DialogManager.info("ScriptExecutionSuc", localizator);
      }
    }
  }

  @FXML
  public void head() {
    try {
      sendCommand(new CommandDto("head", null));
      int responseCount = Integer.parseInt(ois.readUTF());
      StringBuilder response = new StringBuilder();
      for (int i = 0; i < responseCount; i++) {
        response.append(ois.readUTF());
        if (i < responseCount - 1) response.append("\n");
      }

      DialogManager.createAlert(localizator.getKeyString("Head"), response.toString(), Alert.AlertType.INFORMATION, false);
    } catch (IOException e) {
      DialogManager.alert("UnavailableError", localizator);
    }
  }

  @FXML
  public void addIfMax() {
    editController.clear();
    editController.show();
    Route route = editController.getRoute();
    if (route != null) {
      try {
        sendCommand(new CommandDto("add_if_max", route));
        int responseCount = Integer.parseInt(ois.readUTF());
        StringBuilder response = new StringBuilder();
        for (int i = 0; i < responseCount; i++) {
          response.append(ois.readUTF());
          if (i < responseCount - 1) response.append("\n");
        }

        loadCollection();
        DialogManager.createAlert(localizator.getKeyString("Add"), response.toString(), Alert.AlertType.INFORMATION, false);
      } catch (IOException e) {
        DialogManager.alert("UnavailableError", localizator);
      }
    }
  }

  @FXML
  public void addIfMin() {
    editController.clear();
    editController.show();
    Route route = editController.getRoute();
    if (route != null) {
      try {
        sendCommand(new CommandDto("add_if_min", route));
        int responseCount = Integer.parseInt(ois.readUTF());
        StringBuilder response = new StringBuilder();
        for (int i = 0; i < responseCount; i++) {
          response.append(ois.readUTF());
          if (i < responseCount - 1) response.append("\n");
        }

        loadCollection();
        DialogManager.createAlert(localizator.getKeyString("Add"), response.toString(), Alert.AlertType.INFORMATION, false);
      } catch (IOException e) {
        DialogManager.alert("UnavailableError", localizator);
      }
    }
  }

  @FXML
  public void sumOfDistance() {
    try {
      sendCommand(new CommandDto("sum_of_distance", null));
      int responseCount = Integer.parseInt(ois.readUTF());
      StringBuilder response = new StringBuilder();
      for (int i = 0; i < responseCount; i++) {
        response.append(ois.readUTF());
        if (i < responseCount - 1) response.append("\n");
      }

      DialogManager.createAlert(localizator.getKeyString("SumOfDistance"), response.toString(), Alert.AlertType.INFORMATION, false);
    } catch (IOException e) {
      DialogManager.alert("UnavailableError", localizator);
    }
  }

  @FXML
  public void filterByDistance() {
    var dialogDistance = new TextInputDialog();
    dialogDistance.setTitle(localizator.getKeyString("FilterByDistance"));
    dialogDistance.setHeaderText(null);
    dialogDistance.setContentText(localizator.getKeyString("DialogDistance"));
    var distance = dialogDistance.showAndWait();

    if (distance.isPresent() && !distance.get().trim().equals("")) {
      try {
        sendCommand(new CommandDto("filter_by_distance", Integer.parseInt(distance.orElse(""))));
        int responseCount = Integer.parseInt(ois.readUTF());
        StringBuilder response = new StringBuilder();
        for (int i = 0; i < responseCount; i++) {
          response.append(ois.readUTF());
          if (i < responseCount - 1) response.append("\n");
        }

        DialogManager.createAlert(localizator.getKeyString("FilterByDistance"), response.toString(), Alert.AlertType.INFORMATION, true);
      } catch (IOException e) {
        DialogManager.alert("UnavailableError", localizator);
      } catch (NumberFormatException e) {
        DialogManager.alert("NumberFormatException", localizator);
      }
    }
  }

  @FXML
  public void filterContainsName() {
    var dialogName = new TextInputDialog();
    dialogName.setTitle(localizator.getKeyString("FilterContainsName"));
    dialogName.setHeaderText(null);
    dialogName.setContentText(localizator.getKeyString("Name") + ": ");
    var name = dialogName.showAndWait();

    if (name.isPresent() && !name.get().trim().equals("")) {
      try {
        sendCommand(new CommandDto("filter_contains_name", name.get().trim()));
        int responseCount = Integer.parseInt(ois.readUTF());
        StringBuilder response = new StringBuilder();
        for (int i = 0; i < responseCount; i++) {
          response.append(ois.readUTF());
          if (i < responseCount - 1) response.append("\n");
        }

        DialogManager.createAlert(localizator.getKeyString("FilterContainsName"), response.toString(), Alert.AlertType.INFORMATION, true);
      } catch (IOException e) {
        DialogManager.alert("UnavailableError", localizator);
      }
    }
  }

  public void refresh() {
    Thread refresher = new Thread(() -> {
      while (isRefreshing()) {
        Platform.runLater(this::loadCollection);
        try {
          Thread.sleep(10_000);
        } catch (InterruptedException ignored) {
          Thread.currentThread().interrupt();
          System.out.println("Thread was interrupted, Failed to complete operation");
        }
      }
    });
    refresher.setDaemon(true);
    refresher.start();
  }

  public void visualise(boolean refresh) {
    visualPane.getChildren().clear();
    infoMap.clear();

    for (var route : tableTable.getItems()) {
      // Используем creatorId вместо creator
      long creatorId = route.getCreatorId();
      String creatorKey = String.valueOf(creatorId);

      // Генерируем цвет для создателя, если его еще нет
      if (!colorMap.containsKey(creatorKey)) {
        var r = random.nextDouble();
        var g = random.nextDouble();
        var b = random.nextDouble();
        if (Math.abs(r - g) + Math.abs(r - b) + Math.abs(b - g) < 0.6) {
          r += (1 - r) / 1.4;
          g += (1 - g) / 1.4;
          b += (1 - b) / 1.4;
        }
        colorMap.put(creatorKey, Color.color(r, g, b));
      }

      // Размер круга основан на дистанции
      var size = Math.min(125, Math.max(75, route.getDistance() / 10.0));
      var circle = new Circle(size, colorMap.get(creatorKey));
      Color circleColor = colorMap.get(creatorKey);

      // Позиционирование на основе координат
      double x = Math.abs(route.getCoordinates().getX());
      while (x >= 720) x = x / 10;

      double y = Math.abs(route.getCoordinates().getY());
      while (y >= 370) y = y / 3;
      if (y < 100) y += 125;

      var id = new Text('#' + String.valueOf(route.getId()));
      var info = new Label(new RoutePresenter(localizator).describe(route));

      info.setVisible(false);
      circle.setOnMouseClicked(mouseEvent -> {
        if (mouseEvent.getClickCount() == 2) {
          doubleClickUpdate(route);
        }
      });

      circle.setOnMouseEntered(mouseEvent -> {
        id.setVisible(false);
        info.setVisible(true);
        circle.setFill(circleColor.brighter());
      });

      circle.setOnMouseExited(mouseEvent -> {
        id.setVisible(true);
        info.setVisible(false);
        circle.setFill(circleColor);
      });

      id.setFont(Font.font("Segoe UI", size / 1.4));
      info.setStyle("-fx-background-color: white; -fx-border-color: #c0c0c0; -fx-border-width: 2");
      info.setFont(Font.font("Segoe UI", 15));

      visualPane.getChildren().add(circle);
      visualPane.getChildren().add(id);

      infoMap.put(route.getId(), info);
      if (!refresh) {
        var path = new Path();
        path.getElements().add(new MoveTo(-500, -150));
        path.getElements().add(new HLineTo(x));
        path.getElements().add(new VLineTo(y));
        id.translateXProperty().bind(circle.translateXProperty().subtract(id.getLayoutBounds().getWidth() / 2));
        id.translateYProperty().bind(circle.translateYProperty().add(id.getLayoutBounds().getHeight() / 4));
        info.translateXProperty().bind(circle.translateXProperty().add(circle.getRadius()));
        info.translateYProperty().bind(circle.translateYProperty().subtract(120));
        var transition = new PathTransition();
        transition.setDuration(Duration.millis(750));
        transition.setNode(circle);
        transition.setPath(path);
        transition.setOrientation(PathTransition.OrientationType.NONE);
        transition.play();
      } else {
        circle.setCenterX(x);
        circle.setCenterY(y);
        info.translateXProperty().bind(circle.centerXProperty().add(circle.getRadius()));
        info.translateYProperty().bind(circle.centerYProperty().subtract(120));
        id.translateXProperty().bind(circle.centerXProperty().subtract(id.getLayoutBounds().getWidth() / 2));
        id.translateYProperty().bind(circle.centerYProperty().add(id.getLayoutBounds().getHeight() / 4));
        var darker = new FillTransition(Duration.millis(750), circle);
        darker.setFromValue(circleColor);
        darker.setToValue(circleColor.darker().darker());
        var brighter = new FillTransition(Duration.millis(750), circle);
        brighter.setFromValue(circleColor.darker().darker());
        brighter.setToValue(circleColor);
        var transition = new SequentialTransition(darker, brighter);
        transition.play();
      }
    }

    for (var id : infoMap.keySet()) {
      visualPane.getChildren().add(infoMap.get(id));
    }
  }

  private void loadCollection() {
    try {
      sendCommand(new CommandDto("show", null));
      int responseCount = Integer.parseInt(ois.readUTF());
      
      // Десериализуем коллекцию Route объектов
      @SuppressWarnings("unchecked")
      List<Route> routes = (List<Route>) ois.readObject();
      setCollection(routes);
      visualise(true);
    } catch (SocketTimeoutException e) {
      DialogManager.alert("RefreshLost", localizator);
    } catch (IOException | ClassNotFoundException e) {
      DialogManager.alert("UnavailableError", localizator);
    }
  }

  private void doubleClickUpdate(Route route) {
    doubleClickUpdate(route, true);
  }

  private void doubleClickUpdate(Route route, boolean ignoreAnotherUser) {
    if (ignoreAnotherUser && route.getCreatorId() != SessionHandler.getCurrentUser().getId()) return;

    try {
      // Фаза 1: отправляем ID для проверки
      sendCommand(new CommandDto("update_id", route.getId()));
      int responseCount1 = Integer.parseInt(ois.readUTF());
      String response1 = ois.readUTF();
      
      if (!response1.equals("Готово к обновлению")) {
        DialogManager.createAlert(localizator.getKeyString("Update"), response1, Alert.AlertType.ERROR, false);
        return;
      }

      // Открываем форму редактирования
      editController.fill(route);
      editController.show();

      var updatedRoute = editController.getRoute();
      if (updatedRoute != null) {
        // Устанавливаем правильный ID
        updatedRoute.setId(route.getId());
        
        // Фаза 2: отправляем обновленный Route
        sendCommand(new CommandDto("update_id", updatedRoute));
        int responseCount2 = Integer.parseInt(ois.readUTF());
        String response2 = ois.readUTF();

        loadCollection();
        DialogManager.createAlert(localizator.getKeyString("Update"), response2, Alert.AlertType.INFORMATION, false);
      }
    } catch (IOException e) {
      DialogManager.alert("UnavailableError", localizator);
    }
  }

  public void changeLanguage() {
    userLabel.setText(localizator.getKeyString("UserLabel") + " " + SessionHandler.getCurrentUser().getLogin());

    exitButton.setText(localizator.getKeyString("Exit"));
    logoutButton.setText(localizator.getKeyString("LogOut"));
    helpButton.setText(localizator.getKeyString("Help"));
    infoButton.setText(localizator.getKeyString("Info"));
    addButton.setText(localizator.getKeyString("Add"));
    updateButton.setText(localizator.getKeyString("Update"));
    removeByIdButton.setText(localizator.getKeyString("RemoveByID"));
    clearButton.setText(localizator.getKeyString("Clear"));
    executeScriptButton.setText(localizator.getKeyString("ExecuteScript"));
    headButton.setText(localizator.getKeyString("Head"));
    addIfMaxButton.setText(localizator.getKeyString("AddIfMax"));
    addIfMinButton.setText(localizator.getKeyString("AddIfMin"));
    sumOfDistanceButton.setText(localizator.getKeyString("SumOfDistance"));
    filterByDistanceButton.setText(localizator.getKeyString("FilterByDistance"));
    filterContainsNameButton.setText(localizator.getKeyString("FilterContainsName"));

    tableTab.setText(localizator.getKeyString("TableTab"));
    visualTab.setText(localizator.getKeyString("VisualTab"));

    ownerColumn.setText(localizator.getKeyString("Owner"));
    nameColumn.setText(localizator.getKeyString("Name"));
    dateColumn.setText(localizator.getKeyString("CreationDate"));
    fromColumn.setText(localizator.getKeyString("From"));
    toColumn.setText(localizator.getKeyString("To"));
    distanceColumn.setText(localizator.getKeyString("Distance"));

    editController.changeLanguage();

    loadCollection();
  }

  public void setCollection(List<Route> collection) {
    this.collection = collection;
    tableTable.setItems(FXCollections.observableArrayList(collection));
  }

  public void setAuthCallback(Runnable authCallback) {
    this.authCallback = authCallback;
  }

  public void setContext(Socket socket, ObjectInputStream ois, ObjectOutputStream oos,
                         Localizator localizator, Stage stage) {
    this.socket = socket;
    this.ois = ois;
    this.oos = oos;
    this.localizator = localizator;
    this.stage = stage;

    languageComboBox.setValue(SessionHandler.getCurrentLanguage());
    localizator.setBundle(ResourceBundle.getBundle("locales/gui", localeMap.get(SessionHandler.getCurrentLanguage())));
    changeLanguage();

    userLabel.setText(localizator.getKeyString("UserLabel") + " " + SessionHandler.getCurrentUser().getLogin());
  }

  public boolean isRefreshing() {
    return isRefreshing;
  }

  public void setRefreshing(boolean refreshing) {
    isRefreshing = refreshing;
  }

  public void setEditController(EditController editController) {
    this.editController = editController;
    editController.changeLanguage();
  }

  private void sendCommand(CommandDto command) throws IOException {
    oos.writeObject(command);
    oos.flush();
  }
}