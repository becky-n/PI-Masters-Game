package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.DraggableMaker;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.MapRooms;

/**
 * The SafeController class is responsible for managing the interactions and
 * events within the safe scene of the application. It handles the
 * initialization
 * of the scene, including setting up the timer, menu navigation, chat, and
 * loading
 * the clue menu and hints box. It also manages the draggable key and its
 * interaction
 * with the target, as well as handling various button click events.
 */
public class SafeController extends MapRooms {
  public static boolean unlocked = false;
  private static GameStateContext context = new GameStateContext();

  /**
   * Handles the clue menu button click event.
   *
   * @param pane the pane to display the clue menu
   * @throws IOException if there is an I/O error
   */
  public static boolean isUnlocked() {
    return unlocked;
  }

  @FXML
  private ImageView key;
  @FXML
  private Rectangle target;

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
  private Circle redCircle;
  @FXML
  private ImageView clock;
  @FXML
  private Pane mutePane;
  @FXML
  private Pane mapPane;
  @FXML
  private ImageView pattern;
  @FXML
  private ImageView box;
  @FXML
  private Button backButton;
  @FXML
  private Rectangle infoBox;
  @FXML
  private ImageView mapClose;

  private MediaPlayer buttonClickSound;

  /**
   * Initializes the SafeController. Sets up the timer, menu navigation, chat,
   * and loads the clue menu and hints box.
   *
   * @throws IOException if there is an I/O error during initialization
   */
  @FXML
  private void initialize() throws IOException {
    context.setState(context.getGuessingState());

    Media buttonClickMedia = new Media(getClass().getResource("/sounds/click.mp3").toString());
    buttonClickSound = new MediaPlayer(buttonClickMedia);

    // create array of sounds and store
    App.handleMute(mutePane);
    ArrayList<MediaPlayer> sounds = new ArrayList<MediaPlayer>();
    sounds.add(buttonClickSound);

    App.setSounds(sounds);
    App.muteSound();

    App.animateText("There appears to be a key! Would this unlock the jewellery box?", infoLabel);
    // load the clue menu
    App.handleClueMenu(clueMenu);

    // set circle colour for time almost out
    App.setRedCircle(redCircle, clock);

    App.loadHintsBox(instructionsPane);

    App.timer(timerLabel);

    App.mapHoverImage(mapClose);

    // Make the key draggable
    DraggableMaker dm = new DraggableMaker();
    dm.makeDraggable(key);

    key.setOnMouseReleased(event -> {
      // Check if the key is over the target, set scene
      if (isKeyOverTarget(key, target)) {
        unlocked = true;
        try {
          App.setRoot("lock");
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });

  }

  /**
   * Handles the clue menu button click event.
   *
   * @param pane the pane to display the clue menu
   * @throws IOException if there is an I/O error
   */
  private boolean isKeyOverTarget(ImageView key, Rectangle target) {
    // Check if the key is over the target
    return key.getBoundsInParent().intersects(target.getBoundsInParent());
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
    buttonClickSound.play();
    App.guessClick();
  }

  /**
   * Handles the hover event.
   *
   * @param event the mouse event triggered by hovering over a clue
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onHover(MouseEvent event) throws IOException {

    Rectangle clickedRectangle = (Rectangle) event.getSource();
    context.handleClueClick(event, clickedRectangle.getId());
  }

  /**
   * Handles the hover event.
   *
   * @param event the mouse event triggered by hovering over a clue
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void offHover(MouseEvent event) throws IOException {
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    context.handleClueClick(event, clickedRectangle.getId());
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
      infoLabel.toBack();
      infoBox.toBack();
      target.toBack();
      key.toBack();
      pattern.toBack();
      box.toBack();

      App.loadMap(mapPane, this);
      MapController.toggleMapOpen();
    }
  }

  /**
   * Handles the back button click event.
   */
  @Override
  public void onMapBack() {
    MapController.toggleMapOpen();

    box.toFront();
    pattern.toFront();
    target.toFront();
    infoBox.toFront();
    infoLabel.toFront();
    backButton.toFront();
    mutePane.toFront();
    key.toFront();
  }
}