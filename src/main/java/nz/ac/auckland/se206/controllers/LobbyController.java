package nz.ac.auckland.se206.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.util.Duration;
import nz.ac.auckland.se206.Navigation;
import nz.ac.auckland.se206.TimerManager;

public class LobbyController {
  @FXML
  private MenuButton menuButton;
  @FXML
  private Label timerLabel;

  @FXML
  private void initialize() {
    // Initialize the controller
    Navigation nav = new Navigation();
    nav.setMenu(menuButton);

    TimerManager timerManager = TimerManager.getInstance();
    timerManager.start(120);

    Timeline updateTimerTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
      int timeRemaining = timerManager.getTimeRemaining();
      timerLabel.setText(String.format("%02d:%02d",
          timeRemaining / 60, timeRemaining % 60));
    }));
    updateTimerTimeline.setCycleCount(Timeline.INDEFINITE);
    updateTimerTimeline.play();
  }
}