package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.MapRooms;

/**
 * Controller class for the Crime scene.
 * Manages the initialization of the scene, handling button clicks, and updating
 * UI elements.
 */
public class CrimeController extends MapRooms {
  private static GameStateContext context = new GameStateContext();
  // set clues to false by default
  private static boolean safe1 = false;
  private static boolean glass1 = false;
  private static boolean letter1 = false;

  /**
   * Resets the clues by setting the safe, glass, and letter variables to false.
   * This method is typically used to clear any previously set clues and start
   * fresh.
   */
  public static void resetClues() {
    // reset all of the clues to false (not found)
    safe1 = false;
    glass1 = false;
    letter1 = false;
  }

  /**
   * This method returns an array of boolean values representing whether
   * specific clues have been guessed. The array contains three elements:
   * <ul>
   * <li>clues[0] - represents whether clue1 (safe) has been guessed</li>
   * <li>clues[1] - represents whether clue2 (glass) has been guessed</li>
   * <li>clues[2] - represents whether clue3 (letter) has been guessed</li>
   * </ul>
   *
   * @return a boolean array where each element indicates if a corresponding clue
   *         has been guessed
   */
  public static boolean[] cluesGuessed() {
    boolean[] clues = new boolean[3];

    clues[0] = safe1; // represents clue1
    clues[1] = glass1; // represents clue2
    clues[2] = letter1; // represents clue3
    return clues;
  }

  @FXML
  private Label timerLabel;
  @FXML
  private Pane clueMenu;
  @FXML
  private ImageView safeGlow;
  @FXML
  private ImageView glassPileGlow;
  @FXML
  private ImageView invitationGlow;
  @FXML
  private Pane instructionsPane;
  @FXML
  private Pane mapPane;
  @FXML
  private Circle redCircle;
  @FXML
  private ImageView clock;
  @FXML
  private Pane mutePane;
  @FXML
  private ImageView glassPile;
  @FXML
  private Rectangle safe;
  @FXML
  private Rectangle glass;
  @FXML
  private Rectangle letter;
  @FXML
  private ImageView mapClose;

  // Sound effects
  private MediaPlayer buttonClickSound;
  private MediaPlayer twinkleSound;
  private MediaPlayer paperSound;
  private MediaPlayer glassSound;
  private MediaPlayer boxSound;

  /**
   * Initializes the CrimeController. Sets up the timer, menu navigation, chat,
   * and loads the clue menu and hints box.
   * 
   * @throws IOException if there is an I/O error during initialization
   */
  @FXML
  private void initialize() throws IOException {
    // set circle colour for time almost out
    App.setRedCircle(redCircle, clock);

    // Initialize media resources using MediaPlayer
    Media buttonClickMedia = new Media(getClass().getResource("/sounds/click.mp3").toString());
    Media twinkleMedia = new Media(getClass().getResource("/sounds/twinkle.mp3").toString());
    Media paperMedia = new Media(getClass().getResource("/sounds/paper.mp3").toString());
    Media glassMedia = new Media(getClass().getResource("/sounds/glass.mp3").toString());
    Media boxMedia = new Media(getClass().getResource("/sounds/box.mp3").toString());

    // Initialize all MediaPlayer instances
    buttonClickSound = new MediaPlayer(buttonClickMedia);
    twinkleSound = new MediaPlayer(twinkleMedia);
    paperSound = new MediaPlayer(paperMedia);
    glassSound = new MediaPlayer(glassMedia);
    boxSound = new MediaPlayer(boxMedia);

    // create array of sounds and store
    App.handleMute(mutePane);
    ArrayList<MediaPlayer> sounds = new ArrayList<MediaPlayer>();
    sounds.add(buttonClickSound);
    sounds.add(twinkleSound);
    sounds.add(paperSound);
    sounds.add(glassSound);
    sounds.add(boxSound);

    App.setSounds(sounds);
    App.muteSound();

    // set hover effects invisible
    safeGlow.setVisible(false);
    glassPileGlow.setVisible(false);
    invitationGlow.setVisible(false);

    // load interface elements
    App.handleClueMenu(clueMenu);

    // load hints box
    App.loadHintsBox(instructionsPane);

    App.timer(timerLabel);

    App.mapHoverImage(mapClose);

  }

  @FXML
  private void handleMapClick(MouseEvent event) throws IOException {
    buttonClickSound.seek(javafx.util.Duration.ZERO);
    buttonClickSound.play();
    App.loadMap(mapPane, this);

    // Disable interactions with clue rectangles
    safe.setDisable(true);
    glass.setDisable(true);
    letter.setDisable(true);

    // Optionally hide the glow effects or set them invisible
    safeGlow.setVisible(false);
    glassPileGlow.setVisible(false);
    invitationGlow.setVisible(false);

  }

  /**
   * Handles the hover event for the clues.
   *
   * @param event the mouse event triggered by hovering over a clue
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onHover(MouseEvent event) throws IOException {
    // handle hover for clues
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    context.handleClueClick(event, clickedRectangle.getId());
    if (clickedRectangle.getId().equals("safe")) {
      boxSound.play();
      safeGlow.setVisible(true);
    } else if (clickedRectangle.getId().equals("glass")) {
      glassSound.play();
      glassPileGlow.setVisible(true);
      glassPile.setVisible(false);
    } else if (clickedRectangle.getId().equals("letter")) {
      paperSound.play();
      invitationGlow.setVisible(true);
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
    // handle mouse off hover for clues
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    context.handleClueClick(event, clickedRectangle.getId());
    if (clickedRectangle.getId().equals("safe")) {
      boxSound.stop();
      safeGlow.setVisible(false);
    } else if (clickedRectangle.getId().equals("glass")) {
      glassSound.stop();
      glassPileGlow.setVisible(false);
      glassPile.setVisible(true);
    } else if (clickedRectangle.getId().equals("letter")) {
      paperSound.stop();
      invitationGlow.setVisible(false);
    }
  }

  /**
   * Handles the click event for the clues.
   *
   * @param event the mouse event triggered by clicking a clue
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleClueClick(MouseEvent event) throws IOException {
    buttonClickSound.seek(javafx.util.Duration.ZERO);
    twinkleSound.seek(javafx.util.Duration.ZERO);
    buttonClickSound.play();
    twinkleSound.play();

    // get clicked rectangle and handle click
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    context.handleClueClick(event, clickedRectangle.getId());

    // set clue scene based on clicked rectangle
    if (clickedRectangle.getId().equals("safe")) {
      // if clue is found set to different scene
      if (LockController.isBoxUnlocked()) {
        App.setRoot("unlockBox");
        return;
      }
      App.setRoot("safe");
      safe1 = true;
      return;

    }
    if (clickedRectangle.getId().equals("glass")) {
      App.setRoot("window");
      glass1 = true;
      return;
    }
    if (clickedRectangle.getId().equals("letter")) {
      boolean isBurnt = LetterCloseUpController.burnt;
      // if clue is found set to different scene
      if (isBurnt) {
        App.setRoot("letterCloseUp");
        return;
      }
      App.setRoot("letter");
      letter1 = true;
      return;
    }

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

  /**
   * Handles the back button click event.
   */
  @Override
  public void onMapBack() {
    // enable interactions with clue rectangles
    safe.setDisable(false);
    glass.setDisable(false);
    letter.setDisable(false);
  }

}