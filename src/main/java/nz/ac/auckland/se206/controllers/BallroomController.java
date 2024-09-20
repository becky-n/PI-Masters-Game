package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.InstructionsManager;
import nz.ac.auckland.se206.Navigation;

public class BallroomController {
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
    App.handleClueMenu(clueMenu);

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

    App.timer(timerLabel);
  }

  /**
   * Handles the guess button click event.
   *
   * @param event the action event triggered by clicking the guess button
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onHandleGuessClick(ActionEvent event) throws IOException {
    buttonClickSound.play();
    App.guessClick();
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