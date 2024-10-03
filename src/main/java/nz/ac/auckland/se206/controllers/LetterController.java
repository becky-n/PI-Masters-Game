package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.shape.Circle;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.Navigation;

public class LetterController {

  // Static Fields
  public static boolean burnt;

  // Instance Fields
  private MediaPlayer buttonClickSound;

  // FXML-Annotated Fields
  @FXML
  private MenuButton menuButton;
  @FXML
  private Label timerLabel;
  @FXML
  private Circle redCircle;
  @FXML
  private ImageView clock;
  @FXML
  private Pane clueMenu;
  @FXML
  private Label infoLabel;
  @FXML
  private Pane instructionsPane;
  @FXML
  private ImageView envelope;
  @FXML
  private Pane mutePane;

  /**
   * Initializes the LetterController. Sets up the timer, menu navigation, chat,
   * and loads the clue menu and hints box.
   * 
   * @throws IOException if there is an I/O error during initialization
   */
  public void initialize() throws IOException {
    // set circle colour for time almost out
    App.setRedCircle(redCircle, clock);
    
    // Load the sound effects
    Media buttonClickMedia = new Media(getClass().getResource("/sounds/click.mp3").toString());
    buttonClickSound = new MediaPlayer(buttonClickMedia);

    // create array of sounds and store
    App.handleMute(mutePane);
    ArrayList<MediaPlayer> sounds = new ArrayList<MediaPlayer>();
    sounds.add(buttonClickSound);
    
    App.setSounds(sounds);
    App.muteSound();

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
    buttonClickSound.seek(javafx.util.Duration.ZERO); 

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
    buttonClickSound.seek(javafx.util.Duration.ZERO); 
    buttonClickSound.play();
    App.guessClick();
  }
}