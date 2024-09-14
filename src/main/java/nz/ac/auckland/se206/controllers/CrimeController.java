package nz.ac.auckland.se206.controllers;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.ClueMenu;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.Navigation;
import nz.ac.auckland.se206.TimerManager;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.locks.Lock;

public class CrimeController {
  private static GameStateContext context = new GameStateContext();

  private static boolean safe = false;
  private static boolean glass = false;
  private static boolean letter = false;

  private AudioClip buttonClickSound;
  private AudioClip twinkleSound;

  @FXML
  private MenuButton menuButton;
  @FXML
  private Label timerLabel;
  @FXML
  private Pane clueMenu;

  @FXML
  private void initialize() {
    buttonClickSound = new AudioClip(getClass().getResource("/sounds/click.mp3").toString());
    twinkleSound = new AudioClip(getClass().getResource("/sounds/twinkle.mp3").toString());

    try {
      handleClueMenu(clueMenu);
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Initialize the controller
    Navigation nav = new Navigation();
    nav.setMenu(menuButton);

    TimerManager timerManager = TimerManager.getInstance();

    // Bind the timerLabel to the timeRemaining property
    timerLabel.textProperty().bind(
        Bindings.createStringBinding(() -> String.format("%02d:%02d",
            timerManager.getTimeRemaining() / 60,
            timerManager.getTimeRemaining() % 60),
            timerManager.timeRemainingProperty()));
  }

  @FXML
  private void handleClueClick(MouseEvent event) throws IOException {
    twinkleSound.play();

    Rectangle clickedRectangle = (Rectangle) event.getSource();
    context.handleClueClick(event, clickedRectangle.getId());

    if (clickedRectangle.getId().equals("safe")) {
      if(LockController.isBoxUnlocked()) {
        App.setRoot("unlockBox");
        return;
      }
      App.setRoot("safe");
      safe = true;
      return;
    }
    if (clickedRectangle.getId().equals("glass")) {
      App.setRoot("window");
      glass = true;
      return;
    }
    if (clickedRectangle.getId().equals("letter")) {
      letter = true;
    }

    App.setRoot("crime");

  }

  /**
   * Handles the guess button click event.
   *
   * @param event the action event triggered by clicking the guess button
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleGuessClick(ActionEvent event) throws IOException {
    buttonClickSound.play();
    App.setRoot("guess");
    context.handleGuessClick();
  }

  @FXML
  public void handleClueMenu(Pane pane) throws IOException {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/clueMenu.fxml"));
    Pane menuPane = loader.load();

    pane.getChildren().clear();
    pane.getChildren().add(menuPane);

  }

  public static boolean[] cluesGuessed() {
    boolean[] clues = new boolean[3];
    clues[0] = safe; // represents clue1
    clues[1] = glass; // represents clue2
    clues[2] = letter; // represents clue3
    return clues;
  }
}