package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.Navigation;

public class UnlockBoxController {

  @FXML
  private MenuButton menuButton;
  @FXML
  private Label timerLabel;
  @FXML
  private Pane clueMenu;
  @FXML
  private Label infoLabel;
  @FXML
  private ImageView closeUp;
  @FXML
  private Pane instructionsPane;

  private AudioClip buttonClickSound;
  private AudioClip twinkleSound;

  /**
   * Initializes the UnlockBoxController. Sets up the timer, menu navigation,
   * chat,
   * and loads the clue menu and hints box.
   *
   * @throws IOException if there is an I/O error during initialization
   */
  @FXML
  private void initialize() throws IOException {
    // Load the sound effects
    buttonClickSound = new AudioClip(getClass().getResource("/sounds/click.mp3").toString());
    twinkleSound = new AudioClip(getClass().getResource("/sounds/twinkle.mp3").toString());

    App.animateText("A white hair on the empty ring box, who does it belong to?", infoLabel);

    // Load the clue menu
    App.handleClueMenu(clueMenu);

    App.loadHintsBox(instructionsPane);

    // Initialize the controller
    Navigation nav = new Navigation();
    nav.setMenu(menuButton);

    App.timer(timerLabel);

  }

  /**
   * Handles the back button click event.
   *
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onBack() throws IOException {
    buttonClickSound.play();
    App.setRoot("crime");
  }

  /**
   * Handles the guess button click event.
   *
   * @param event the action event triggered by clicking the guess button
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onHandleGuessClick(ActionEvent event) throws IOException {
    // play the button click sound
    buttonClickSound.play();
    App.guessClick();

  }

  /**
   * Loads the hints box into the specified pane.
   * 
   * @param pane the pane to which the hints box should be added
   * @throws IOException if there is an I/O error during loading the hints box
   */
  @FXML
  private void handleCloseUp() {
    twinkleSound.play();
    closeUp.setImage(new Image("/images/HairClose.png"));

  }

  /**
   * Handles the close button click event.
   * 
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleCloseOut() {
    twinkleSound.stop();
    closeUp.setImage(null);

  }

}
