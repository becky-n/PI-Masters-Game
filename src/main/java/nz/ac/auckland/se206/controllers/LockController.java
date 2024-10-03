package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.Navigation;

/**
 * Controller class for the Lock scene.
 * Manages interactions and updates for the Lock view, including handling the
 * timer,
 * navigating menus, and updating hints.
 */
public class LockController {
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
   * Resets the lock.
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
  private Pane mutePane;

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
    
    App.setSounds(sounds);
    App.muteSound();

    // Hide the glow effect
    leftGlow.setVisible(false);
    rightGlow.setVisible(false);

    App.animateText("Try rotating the key, is there a pattern needed to unlock the box?", infoLabel);

    App.handleClueMenu(clueMenu);

    App.loadHintsBox(instructionsPane);

    // Initialize the controller
    Navigation nav = new Navigation();
    nav.setMenu(menuButton);

    App.timer(timerLabel);

  }

  /**
   * Handles the clue menu button click event.
   *
   * @param pane the pane to display the clue menu
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleLeftRotate() {
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
    buttonClickSound.play();
    App.guessClick();

  }
}
