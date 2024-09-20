package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.Navigation;

public class LetterController {

  // Static Fields
  public static boolean burnt;

  // Instance Fields
  private AudioClip buttonClickSound;

  // FXML-Annotated Fields
  @FXML
  private MenuButton menuButton;

  @FXML
  private Label timerLabel;

  @FXML
  private Pane clueMenu;

  @FXML
  private Label infoLabel;

  @FXML
  private Pane instructionsPane;

  @FXML
  private ImageView envelope;

  /**
   * Initializes the LetterController. Sets up the timer, menu navigation, chat,
   * and loads the clue menu and hints box.
   * 
   * @throws IOException if there is an I/O error during initialization
   */
  public void initialize() throws IOException {
    // Load the sound effects
    buttonClickSound = new AudioClip(getClass().getResource("/sounds/click.mp3").toString());
    Navigation nav = new Navigation();
    nav.setMenu(menuButton);
    // Load the clue menu
    App.handleClueMenu(clueMenu);
    // Load the hints box
    App.loadHintsBox(instructionsPane);
    // Load the chat
    App.timer(timerLabel);
    App.animateText("Interesting, I wonder what this envelope contains...", infoLabel);
  }

  /**
   * Handles the envelope click event.
   *
   * @param event the mouse event triggered by clicking the envelope
   * @throws IOException if there is an I/O error
   */
  @FXML
  public void handleEnvelopeClick(MouseEvent event) {
    buttonClickSound.play();
    try {
      App.setRoot("letterCloseUp");
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  /**
   * Handles the back button click event.
   * 
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onBack() throws IOException {
    // Play button click sound
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
    buttonClickSound.play();
    App.guessClick();
  }
}