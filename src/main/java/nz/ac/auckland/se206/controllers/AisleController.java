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

/**
 * Controller class for the Aisle scene.
 * Manages interactions and updates for the Aisle view, including handling the
 * timer,
 * navigating menus, and updating hints.
 */
public class AisleController {

  /**
   * Loads the clue menu into the provided pane.
   * 
   * @param pane the pane where the clue menu will be loaded
   * @throws IOException if there is an I/O error during loading
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
   * Initializes the AisleController. Sets up the timer, menu navigation, chat,
   * and loads the clue menu and hints box.
   * 
   * @throws IOException if there is an I/O error during initialization
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

    // load the chat
    App.openChat("Gerald", chatPane);

    // Initialize the controller
    Navigation nav = new Navigation();
    nav.setMenu(menuButton);

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
   * Loads the hints box into the provided pane.
   * 
   * @param pane the pane where the hints box will be loaded
   * @throws IOException if there is an I/O error during loading
   */
  private void loadHintsBox(Pane pane) throws IOException {
    // Load the hints box
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/instructions.fxml"));
    Pane hintsPane = loader.load();
    pane.getChildren().clear();
    pane.getChildren().add(hintsPane);
  }

  /**
   * Updates the hint text displayed in the hints box.
   * 
   * @param newHint the new hint text to be displayed
   */
  public void updateHint(String newHint) {
    InstructionsManager.getInstance().updateInstructions(newHint);

    // Update the instructions
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/hintsBox.fxml"));
    try {
      InstructionsManager hintsController = loader.getController();
      hintsController.updateInstructions("hello");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}