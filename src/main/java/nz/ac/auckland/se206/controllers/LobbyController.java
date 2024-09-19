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
import nz.ac.auckland.se206.Navigation;
import nz.ac.auckland.se206.TimerManager;

import java.io.IOException;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;

/**
 * Controller class for the Lobby scene.
 * Manages interactions and updates for the Lobby view, including handling the
 * timer,
 * navigating menus, and updating hints.
 */
public class LobbyController {
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

  /**
   * Initializes the LobbyController. Sets up the timer, menu navigation, chat,
   * and loads the clue menu and hints box.
   * 
   * @throws IOException if there is an I/O error during initialization
   */
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
    App.openChat("Jesin", chatPane);

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
    // Check if the player has talked to all suspects and guessed all clues
    boolean[] suspects = ChatController.suspectsTalkedTo();
    boolean[] clues = CrimeController.cluesGuessed();
    if (suspects[0] && suspects[1] && suspects[2]) {
      if (clues[0] || clues[1] || clues[2]) {
        context.handleGuessClick();
        App.setRoot("guess");
      }

    }
  }

  /**
   * Loads the clue menu into the specified pane.
   * 
   * @param pane the pane to which the clue menu should be added
   * @throws IOException if there is an I/O error during loading the clue menu
   */
  @FXML
  public static void handleClueMenu(Pane pane) throws IOException {
    // Load the clue menu
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/clueMenu.fxml"));
    Pane menuPane = loader.load();

    pane.getChildren().clear();
    pane.getChildren().add(menuPane);

  }

  /**
   * Loads the hints box into the specified pane.
   * 
   * @param pane the pane to which the hints box should be added
   * @throws IOException if there is an I/O error during loading the hints box
   */
  private void loadHintsBox(Pane pane) throws IOException {
    // Load the hints box
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/instructions.fxml"));
    Pane hintsPane = loader.load();
    pane.getChildren().clear();
    pane.getChildren().add(hintsPane);
  }

  /**
   * Updates the instructions in the hints box.
   * 
   * @param newHint the new hint to be displayed
   */
  public void updateHint(String newHint) {
    // Update the instructions
    InstructionsManager.getInstance().updateInstructions(newHint);
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/hintsBox.fxml"));
    try {
      InstructionsManager hintsController = loader.getController();
      hintsController.updateInstructions("hello");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}