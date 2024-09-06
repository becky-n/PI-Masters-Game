package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.Navigation;
import nz.ac.auckland.se206.TimerManager;
import java.io.IOException;

public class AisleController {
  @FXML
  private MenuButton menuButton;

  @FXML
  private Label timerLabel;

  private static GameStateContext context = new GameStateContext();

  @FXML
  private void initialize() {
    // Initialize the controller
    Navigation nav = new Navigation();
    nav.setMenu(menuButton);

    TimerManager timerManager = TimerManager.getInstance();

    timerLabel.textProperty().bind(
        Bindings.createStringBinding(() -> String.format("%02d:%02d",
            timerManager.getTimeRemaining() / 60,
            timerManager.getTimeRemaining() % 60),
            timerManager.timeRemainingProperty()));
  }

  /**
   * Handles the guess button click event.
   *
   * @param event the action event triggered by clicking the guess button
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleGuessClick(ActionEvent event) throws IOException {
    context.handleGuessClick();
  }
}