package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.InstructionsManager;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.Navigation;
import nz.ac.auckland.se206.TimerManager;

import java.io.IOException;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import java.io.IOException;

public class BallroomController {
  private AudioClip buttonClickSound;

  @FXML
  private MenuButton menuButton;
  @FXML
  private Label timerLabel;
  @FXML
  private Pane chatPane;

  @FXML
  private Pane clueMenu;

  @FXML
  private Pane instructionsPane;

  private static GameStateContext context = new GameStateContext();

  @FXML
  private void initialize() throws IOException {
    try {
      handleClueMenu(clueMenu);
    } catch (IOException e) {
      e.printStackTrace();
    }

    try {
      loadHintsBox(instructionsPane);
    } catch (IOException e) {
      e.printStackTrace();
    }

    buttonClickSound = new AudioClip(getClass().getResource("/sounds/click.mp3").toString());

    // Initialize the controller
    Navigation nav = new Navigation();
    nav.setMenu(menuButton);

    // load the chat
    App.openChat("Andrea", chatPane);

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
    boolean[] suspects= ChatController.suspectsTalkedTo();
    if(suspects[0] && suspects[1] && suspects[2]){
      context.handleGuessClick();
      App.setRoot("guess");
      
    }
    // App.setRoot("guess");
    // context.handleGuessClick();
  }

  @FXML
  public static void handleClueMenu(Pane pane) throws IOException {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/clueMenu.fxml"));
    Pane menuPane = loader.load();
    pane.getChildren().clear();
    pane.getChildren().add(menuPane);

  }

  private void loadHintsBox(Pane pane) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/instructions.fxml"));
    Pane hintsPane = loader.load();
    // Assuming you want to add it to the root pane or a specific pane in the scene
    pane.getChildren().clear();
    pane.getChildren().add(hintsPane);
  }

  public void updateHint(String newHint) {
    InstructionsManager.getInstance().updateInstructions(newHint);

    // You can update the hint dynamically by accessing the controller
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/hintsBox.fxml"));
    try {
      InstructionsManager hintsController = loader.getController();
      hintsController.updateInstructions("hello");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}