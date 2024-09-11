package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import nz.ac.auckland.se206.GameStateContext;
import java.io.IOException;
import javafx.beans.binding.Bindings;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.Navigation;
import nz.ac.auckland.se206.TimerManager;
import java.io.IOException;
import nz.ac.auckland.se206.App;

public class AisleController {
  private AudioClip buttonClickSound;

  @FXML
  private MenuButton menuButton;
  @FXML
  private Label timerLabel;
  @FXML
  private Pane chatPane;

  @FXML
  private Pane clueMenu;

  private static GameStateContext context = new GameStateContext();

  private void initialize() throws IOException {
      try {
      handleClueMenu(clueMenu);
    } catch (IOException e) {
      e.printStackTrace();
    }

    buttonClickSound = new AudioClip(getClass().getResource("/sounds/click.mp3").toString());
  
    // Initialize the controller
    Navigation nav = new Navigation();
    nav.setMenu(menuButton);

    // load the chat
    App.openChat("Gerald", chatPane);

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
    buttonClickSound.play();
    App.setRoot("guess");
    context.handleGuessClick();
  }

   @FXML
  public static void handleClueMenu(Pane pane) throws IOException {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/clueMenu.fxml"));
    Pane menuPane = loader.load();

    pane.getChildren().clear();
    pane.getChildren().add(menuPane);

  }