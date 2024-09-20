package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
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

public class BallroomController {
  private static GameStateContext context = new GameStateContext();
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

  private AudioClip buttonClickSound;

  /**
   * Controller class for the Ballroom scene.
   * Manages the initialization of the scene, handling button clicks, and updating
   * UI elements.
   */
  @FXML
  private void initialize() throws IOException {
    // Load the clue menu
    try {
      handleClueMenu(clueMenu);
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Load the hints box
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
    // Check if all suspects have been talked to and at least one clue has been
    // found
    boolean[] suspects = ChatController.suspectsTalkedTo();
    boolean[] clues = CrimeController.cluesGuessed();

    // Check if the player has talked to all suspects and guessed all clues
    boolean allSuspectsTalkedTo = suspects[0] && suspects[1] && suspects[2];
    boolean atLeastOneClueFound = clues[0] || clues[1] || clues[2];
    if (suspects[0] && suspects[1] && suspects[2]) {
      if (clues[0] || clues[1] || clues[2]) {
        context.handleGuessClick();
        App.setRoot("guess");
      }
      // Display a hint if the player has talked to all suspects but not found any
      // clues
    } else if (!allSuspectsTalkedTo && atLeastOneClueFound) {
      InstructionsManager.getInstance().updateInstructions(
          "You must talk to all suspects before making a guess."
      );
      InstructionsManager.getInstance().showInstructions();
  } else if (!atLeastOneClueFound && allSuspectsTalkedTo) {
      InstructionsManager.getInstance().updateInstructions(
          "You must find at least one clue before making a guess."
      );
      InstructionsManager.getInstance().showInstructions();
  } else {
      InstructionsManager.getInstance().updateInstructions(
          "You must talk to all suspects and find at least one clue before making a guess."
      );
      InstructionsManager.getInstance().showInstructions();
  }  
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
    // Assuming you want to add it to the root pane or a specific pane in the scene
    pane.getChildren().clear();
    pane.getChildren().add(hintsPane);
  }

  /**
   * Updates the instructions in the hints box.
   * 
   * @param newHint the new hint to display in the hints box
   */
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