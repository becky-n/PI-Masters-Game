package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.Pane;

import java.io.IOException;

import javafx.beans.binding.Bindings;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.Navigation;
import nz.ac.auckland.se206.TimerManager;

public class BallroomController {

  @FXML
  private MenuButton menuButton;
  @FXML
  private Label timerLabel;
  @FXML
  private Pane chatPane;

  @FXML
  private void initialize() throws IOException {
    // Initialize the controller
    Navigation nav = new Navigation();
    nav.setMenu(menuButton);

    // load chat.fxml
    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fxml/chat.fxml"));
    Pane chatContent = fxmlLoader.load();
    chatPane.getChildren().clear();
    chatPane.getChildren().add(chatContent);
    chatPane.setVisible(true);

    TimerManager timerManager = TimerManager.getInstance();

    timerLabel.textProperty().bind(
        Bindings.createStringBinding(() -> String.format("%02d:%02d",
            timerManager.getTimeRemaining() / 60,
            timerManager.getTimeRemaining() % 60),
            timerManager.timeRemainingProperty()));
  }
}