package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.MapRooms;

/**
 * Controller class for the UnlockBox scene.
 */
public class UnlockBoxController extends MapRooms {

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
  @FXML
  private Pane mapPane;
  @FXML
  private ImageView box;
  @FXML
  private Rectangle infoBox;
  @FXML
  private Button backButton;
  @FXML
  private ImageView ringBox;
  @FXML
  private ImageView mapClose;

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

    App.animateText("A white hair on the empty ring box, who does it belong to?", infoLabel);

    App.intialiseControllers(
        clueMenu, redCircle, clock, instructionsPane, sounds, mutePane, timerLabel, mapClose);

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

  /**
   * Handles the event when the map is clicked.
   *
   * @param event the MouseEvent that triggered this handler
   * @throws IOException if an I/O error occurs during the map loading process
   */
  @FXML
  private void handleMapClick(MouseEvent event) throws IOException {
    buttonClickSound.seek(javafx.util.Duration.ZERO);
    buttonClickSound.play();

    if (MapController.mapOpen) {
      App.unloadMap(mapPane, this); // close map
    } else {
      backButton.toBack();
      infoBox.toBack();
      infoLabel.toBack();
      ringBox.toBack();
      closeUp.toBack();

      App.loadMap(mapPane, this);
      MapController.toggleMapOpen();
    }
  }

  /**
   * Handles the back button click event.
   */
  @Override
  public void onMapBack() {
    // close the map
    MapController.toggleMapOpen();
    // bring the elements to the front
    ringBox.toFront();
    closeUp.toFront();
    infoBox.toFront();
    infoLabel.toFront();
    backButton.toFront();
    mutePane.toFront();
  }

}
