package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
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
  @FXML
  private Circle redCircle;
  @FXML
  private ImageView clock;
  @FXML
  private Pane mutePane;

  private MediaPlayer buttonClickSound;
  private MediaPlayer twinkleSound;

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
    Media buttonClickMedia = new Media(getClass().getResource("/sounds/click.mp3").toString());
    Media twinkleMedia = new Media(getClass().getResource("/sounds/twinkle.mp3").toString());

    buttonClickSound = new MediaPlayer(buttonClickMedia);
    twinkleSound = new MediaPlayer(twinkleMedia);

    // create array of sounds and store
    App.handleMute(mutePane);
    ArrayList<MediaPlayer> sounds = new ArrayList<MediaPlayer>();
    sounds.add(buttonClickSound);
    sounds.add(twinkleSound);
    
    App.setSounds(sounds);
    App.muteSound();

    App.animateText("A white hair on the empty ring box, who does it belong to?", infoLabel);

    // Load the clue menu
    App.handleClueMenu(clueMenu);

    // set circle colour for time almost out
    App.setRedCircle(redCircle, clock);

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
    buttonClickSound.seek(javafx.util.Duration.ZERO); 

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
