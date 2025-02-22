package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.MapRooms;

/**
 * Controller class for the Lock scene.
 * Manages interactions and updates for the Lock view, including handling the
 * timer,
 * navigating menus, and updating hints.
 */
public class LockController extends MapRooms {
  public static boolean safeUnlocked = false;
  private static GameStateContext context = new GameStateContext();

  /**
   * Checks if the box is unlocked.
   *
   * @return true if the box is unlocked, false otherwise
   */
  public static boolean isBoxUnlocked() {
    if (safeUnlocked) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Resets the lock to the locked state.
   */
  public static void resetLock() {
    safeUnlocked = false;
  }

  @FXML
  private ImageView key;
  @FXML
  private MenuButton menuButton;
  @FXML
  private Label timerLabel;
  @FXML
  private Pane clueMenu;
  @FXML
  private Label infoLabel;
  @FXML
  private ImageView leftGlow;
  @FXML
  private ImageView rightGlow;
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
  private Rectangle infoBox;
  @FXML
  private Button backButton;
  @FXML
  private Rectangle leftButton;
  @FXML
  private Rectangle rightButton;
  @FXML
  private ImageView left;
  @FXML
  private ImageView right;
  @FXML
  private ImageView mapClose;

  // set the expected sequence to unlock the box
  private List<String> expectedSequence = List.of("left", "left", "right", "left");
  private List<String> userSequence = new ArrayList<>();
  private MediaPlayer buttonClickSound;
  private MediaPlayer unlockSound;
  private MediaPlayer keySound;
  private double angle = 0;

  /**
   * Initializes the LockController. Sets up the timer, menu navigation, chat,
   * and loads the clue menu and hints box.
   *
   * @throws IOException if there is an I/O error during initialization
   */
  @FXML
  public void initialize() throws IOException {
    // Load the sound files
    Media buttonClickMedia = new Media(getClass().getResource("/sounds/click.mp3").toString());
    Media unlockMedia = new Media(getClass().getResource("/sounds/unlock.mp3").toString());
    Media keyMedia = new Media(getClass().getResource("/sounds/key.mp3").toString());

    // Initialize the media players
    buttonClickSound = new MediaPlayer(buttonClickMedia);
    unlockSound = new MediaPlayer(unlockMedia);
    keySound = new MediaPlayer(keyMedia);

    // create array of sounds and store
    App.handleMute(mutePane);
    ArrayList<MediaPlayer> sounds = new ArrayList<MediaPlayer>();
    sounds.add(buttonClickSound);
    sounds.add(unlockSound);
    sounds.add(keySound);

    // Hide the glow effect
    leftGlow.setVisible(false);
    rightGlow.setVisible(false);

    App.animateText(
        "Try rotating the key, is there a pattern needed to unlock the box?", infoLabel);

    App.intialiseControllers(
        clueMenu, redCircle, clock, instructionsPane, sounds, mutePane, timerLabel, mapClose);

  }

  /**
   * Handles the clue menu button click event.
   *
   * @param pane the pane to display the clue menu
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleLeftRotate() {
    keySound.seek(javafx.util.Duration.ZERO);
    keySound.play();
    angle -= 90; // Rotate left by 90 degrees
    rotateImage(15, 75, Rotate.Z_AXIS);
    trackAction("left");
  }

  /**
   * Handles the clue menu button click event.
   *
   * @param pane the pane to display the clue menu
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleRightRotate() {
    keySound.seek(javafx.util.Duration.ZERO);
    keySound.play();
    angle += 90; // Rotate right by 90 degrees
    rotateImage(15, 75, Rotate.Z_AXIS);
    trackAction("right");
  }

  /**
   * Handles the hover event for the clues.
   *
   * @param event the mouse event triggered by hovering over a clue
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onHover(MouseEvent event) throws IOException {
    // set image on hover
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    context.handleClueClick(event, clickedRectangle.getId());
    if (clickedRectangle.getId().equals("leftButton")) {
      leftGlow.setVisible(true);
    } else if (clickedRectangle.getId().equals("rightButton")) {
      rightGlow.setVisible(true);
    }
  }

  /**
   * Handles the off hover event for the clues.
   *
   * @param event the mouse event triggered by moving the mouse off a clue
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void offHover(MouseEvent event) throws IOException {
    // set image when mouse not hovering
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    context.handleClueClick(event, clickedRectangle.getId());
    if (clickedRectangle.getId().equals("leftButton")) {
      leftGlow.setVisible(false);
    } else if (clickedRectangle.getId().equals("rightButton")) {
      rightGlow.setVisible(false);
    }
  }

  /**
   * Handles the clue menu button click event.
   *
   * @param pane the pane to display the clue menu
   * @throws IOException if there is an I/O error
   */
  private void rotateImage(double pivotX, double pivotY, javafx.geometry.Point3D axis) {
    Rotate rotate = new Rotate(angle, pivotX, pivotY);
    // Rotate the key
    rotate.setAxis(axis);
    key.getTransforms().clear();
    key.getTransforms().add(rotate);
  }

  /**
   * Tracks the user's actions and checks if the user has unlocked the box.
   *
   * @param action the action to track
   */
  private void trackAction(String action) {
    userSequence.add(action);

    // Check if the user sequence matches the expected sequence
    if (userSequence.equals(expectedSequence)) {
      unlockSound.play();
      safeUnlocked = true;
      try {
        App.setRoot("unlockBox");
      } catch (IOException e) {
        e.printStackTrace();
      }

      userSequence.clear();
    }

  }

  /**
   * Handles the back button click event.
   *
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onBack() throws IOException {
    // set root to safe
    buttonClickSound.play();
    App.setRoot("safe");
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
    buttonClickSound.seek(javafx.util.Duration.ZERO);
    buttonClickSound.play();
    App.guessClick();

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
      key.toBack();
      leftButton.toBack();
      rightButton.toBack();
      left.toBack();
      right.toBack();
      leftGlow.toBack();
      rightGlow.toBack();

      App.loadMap(mapPane, this);
      MapController.toggleMapOpen();
    }
  }

  /**
   * Handles the back button click event.
   */
  @Override
  public void onMapBack() {
    // close map
    MapController.toggleMapOpen();
    // set the elements to front
    rightGlow.toFront();
    right.toFront();
    leftGlow.toFront();
    left.toFront();
    rightButton.toFront();
    leftButton.toFront();
    infoBox.toFront();
    infoLabel.toFront();
    backButton.toFront();
    key.toFront();
    mutePane.toFront();
  }

}
