package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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
import nz.ac.auckland.se206.TimerManager;

/**
 * The LetterCloseUpController class extends the MapRooms class and provides
 * functionalities specific to the close-up view of a letter in the application.
 * This class manages the state and behavior of the letter close-up, including
 * handling whether the letter has been burnt or not.
 */
public class LetterCloseUpController extends MapRooms {

  // Static Fields
  public static boolean burnt = false;

  // Static Methods
  /**
   * Resets the letter to its original state.
   */
  public static void resetLetter() {
    burnt = false;
  }

  // Instance Fields
  private int envelopeClicked = 0;
  private GraphicsContext gc;
  private boolean displayed = false;
  private boolean isErasing = false;
  private boolean matchBoxClicked = false;
  private MediaPlayer buttonClickSound;
  private MediaPlayer matchSound;

  // FXML-Annotated Fields
  @FXML
  private ImageView envelopeCloseUp;
  @FXML
  private ImageView letterOpened;
  @FXML
  private ImageView letterOpenedReveal;
  @FXML
  private Canvas eraseCanvas;
  @FXML
  private MenuButton menuButton;
  @FXML
  private Label timerLabel;
  @FXML
  private Pane clueMenu;
  @FXML
  private Rectangle matchBox;
  @FXML
  private Circle redCircle;
  @FXML
  private ImageView clock;
  @FXML
  private Label infoLabel;
  @FXML
  private Rectangle instructionsBox;
  @FXML
  private Pane instructionsPane;
  @FXML
  private Rectangle envelopeCloseUpRec;
  @FXML
  private Pane mutePane;
  @FXML
  private ImageView matchCursorOverlay;
  @FXML
  private Pane mapPane;
  @FXML
  private ImageView match;
  @FXML
  private Button backButton;
  @FXML
  private ImageView mapClose;
  @FXML
  private Label nameLabel;
  @FXML
  private Rectangle nameRect;

  /**
   * Initializes the LetterCloseUpController. Sets up the timer, menu navigation,
   * chat,
   * and loads the clue menu and hints box.
   */
  public void initialize() throws IOException {

    // Load the sound effects
    Media buttonClickMedia = new Media(getClass().getResource("/sounds/click.mp3").toString());
    Media matchMedia = new Media(getClass().getResource("/sounds/fire-crackling.wav").toString());

    buttonClickSound = new MediaPlayer(buttonClickMedia);
    matchSound = new MediaPlayer(matchMedia);

    // create array of sounds and store
    App.handleMute(mutePane);
    ArrayList<MediaPlayer> sounds = new ArrayList<MediaPlayer>();
    sounds.add(buttonClickSound);
    sounds.add(matchSound);

    Image image = new Image(getClass().getResource("/images/closed-envelope.png").toString());
    // Set the envelope image
    envelopeCloseUp.setImage(image);

    eraseCanvas.setDisable(true);
    // If the letter not been burnt, set instructions box
    if (burnt == false) {
      App.animateText("let's see what's inside the envelope..", infoLabel);
    }

    App.intialiseControllers(
        clueMenu, redCircle, clock, instructionsPane, sounds, mutePane, timerLabel, mapClose);

    // If the letter has been burnt, set the image to the hidden invitation
    if (burnt == true) {
      Image imageHidden = new Image(getClass().getResource(
          "/images/invitationHidden.png").toString());
      letterOpenedReveal.setImage(imageHidden);
      App.animateText("interesting, I wonder who wrote this...", infoLabel);
      matchBox.setDisable(true);
      envelopeCloseUpRec.setDisable(true);
    }

    // Set the matchCursorOverlay ImageView initially invisible
    matchCursorOverlay.setVisible(false);

    // if time runs out
    TimerManager.getInstance().timeRemainingProperty().addListener((obs, oldTime, newTime) -> {
      if (newTime.intValue() == 0) {
        handleTimerExpired();
      }
    });

  }

  /**
   * Handles the clue menu when the timer expires.
   *
   * @throws IOException if there is an I/O error
   */
  private void handleTimerExpired() {
    matchSound.stop();
  }

  /**
   * Handles the envelope click event. If the envelope is clicked once, open the
   * envelope.
   *
   * @param event the action event triggered by clicking the clue menu
   */
  @FXML
  public void envelopeClicked(MouseEvent event) {
    // play the button click sound
    buttonClickSound.play();
    // If the envelope is clicked once, open the envelope
    if (envelopeClicked == 0) {
      envelopeClicked++;
      letterOpened.setImage(null);
      Image image = new Image(getClass().getResource(
          "/images/open-envelope.png").toString());
      envelopeCloseUp.setImage(image);
      return;
      // If the envelope is opened reveal invitation
    } else {
      instructionsPane.setVisible(false);
      envelopeClicked++;
      Image imageHidden = new Image(getClass().getResource(
          "/images/invitationHidden.png").toString());
      letterOpenedReveal.setImage(imageHidden);
      App.animateText("I wonder what will happen if you burn the paper...", infoLabel);
      // if the envelope has already been burnt
      if (burnt == true) {
        eraseCanvas.setDisable(false);
        return;
      }
      // create canvas for erasing the layer
      createcanvas();

    }
  }

  /**
   * Handles the matchbox click event.
   *
   * @param event the mouse event triggered by clicking the matchbox
   */
  @FXML
  public void handleMatchBoxClick(MouseEvent event) {
    // Check if envelope is open
    if (envelopeClicked < 2) {
      return;
    } else {
      // Set the matchbox clicked state
      matchBoxClicked = true;
      matchSound.seek(javafx.util.Duration.ZERO);
      buttonClickSound.seek(javafx.util.Duration.ZERO);

      buttonClickSound.play();
      matchSound.play();

      // Show the match image overlay and make it follow the cursor
      matchCursorOverlay.setVisible(true);
      matchCursorOverlay.setMouseTransparent(true); // Allow events to pass through it
      matchCursorOverlay.setFitWidth(100);
      matchCursorOverlay.setFitHeight(100);

      // Move the image with the mouse on both move and drag
      matchCursorOverlay.getScene().setOnMouseMoved(
          mouseEvent -> moveImageWithCursor(mouseEvent));
      matchCursorOverlay.getScene().setOnMouseDragged(
          mouseEvent -> moveImageWithCursor(mouseEvent));
    }
  }

  // Helper method to move the image with the cursor
  private void moveImageWithCursor(MouseEvent mouseEvent) {
    matchCursorOverlay.setLayoutX(mouseEvent.getSceneX() - matchCursorOverlay.getFitWidth() / 2);
    matchCursorOverlay.setLayoutY(mouseEvent.getSceneY() - matchCursorOverlay.getFitHeight() / 2);
  }

  /**
   * Handle when the user releases the mouse or completes the action.
   */
  public void matchUseComplete() {
    if (matchBoxClicked) {
      // Hide the match image overlay
      matchCursorOverlay.setVisible(false);
      matchBoxClicked = false; // Reset the state
    }
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
      envelopeCloseUp.toBack();
      letterOpenedReveal.toBack();
      letterOpened.toBack();
      envelopeCloseUpRec.toBack();
      eraseCanvas.toBack();
      match.toBack();
      matchBox.toBack();
      instructionsBox.toBack();
      infoLabel.toBack();
      backButton.toBack();
      App.loadMap(mapPane, this);// open map
      MapController.toggleMapOpen();
    }
  }

  /**
   * Creates the canvas for erasing the opaque layer.
   */
  private void createcanvas() {
    eraseCanvas.setDisable(false);
    eraseCanvas.setWidth(375);
    eraseCanvas.setHeight(528);

    gc = eraseCanvas.getGraphicsContext2D();

    // set canvas as image over hidden image
    Image image = new Image(getClass().getResource("/images/invitation.png").toString());
    gc.drawImage(image, 0, 0, eraseCanvas.getWidth(), eraseCanvas.getHeight());

    // Set up mouse events to erase the opaque layer

    eraseCanvas.setOnMousePressed(this::startErasing);
    eraseCanvas.setOnMouseReleased(this::stopErasing);

    eraseCanvas.setOnMouseDragged(event -> {
      double x = event.getX();
      double y = event.getY();

      erase(event);
      // Check if the click is within a specific area (e.g., a rectangle or a shape)
      if (isInsideArea(x, y) && !displayed) {
        App.animateText("interesting, I wonder who wrote this...", infoLabel);
        displayed = true;
      }
    });

  }

  /**
   * Checks if the given coordinates are inside a specific area.
   *
   * @param x the x-coordinate
   * @param y the y-coordinate
   * @return true if the coordinates are inside the area, false otherwise
   */
  private boolean isInsideArea(double x, double y) {
    double rectX = 200;
    double rectY = 50;
    double rectWidth = 500;
    double rectHeight = 147;
    // adjusting the area to match the text
    return x >= rectX && x <= rectX + rectWidth && y >= rectY && y <= rectY + rectHeight;
  }

  /**
   * Starts erasing when the mouse is pressed.
   */
  private void startErasing(MouseEvent event) {
    if (matchBoxClicked == false) {
      return;
    }
    isErasing = true;

    erase(event); // Erase the point where the mouse is pressed
  }

  /**
   * Erases the layer to reveal the hidden invitation.
   */
  private void erase(MouseEvent event) {
    if (matchBoxClicked == false) {
      return;
    }
    burnt = true;
    if (isErasing) {
      double x = event.getX();
      double y = event.getY();

      // Set the eraser size and clear the area
      double eraserSize = 20; // Adjust the eraser size as needed
      gc.clearRect(x - eraserSize / 2, y - eraserSize / 2, eraserSize, eraserSize);
    }

  }

  /**
   * Stops erasing when the mouse is released.
   */
  @FXML
  private void stopErasing(MouseEvent event) {
    // Stop erasing
    isErasing = false;
  }

  /**
   * Sets the scene back to the crime scene.
   */
  @FXML
  private void onBack() throws IOException {
    buttonClickSound.seek(javafx.util.Duration.ZERO);
    // Play button click sound and stop match sound
    matchSound.stop();
    matchUseComplete();
    buttonClickSound.play();
    // Set the scene back to the crime scene
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
    // Play the button click sound
    buttonClickSound.seek(javafx.util.Duration.ZERO);
    buttonClickSound.play();
    // Check if the guess is successful
    boolean successfulGuess = App.guessClick();
    if (successfulGuess) {
      // Stop the match sound
      matchSound.stop();
    }
  }

  /**
   * Handles the back button click event.
   */
  @Override
  public void onMapBack() {
    // Close the map
    MapController.toggleMapOpen();
    // Set the scene back to the crime scene
    backButton.toFront();
    envelopeCloseUp.toFront();
    letterOpenedReveal.toFront();
    letterOpened.toFront();
    envelopeCloseUpRec.toFront();
    eraseCanvas.toFront();
    match.toFront();
    matchBox.toFront();
    instructionsBox.toFront();
    infoLabel.toFront();
    mutePane.toFront();
    nameRect.toFront();
    nameLabel.toFront();
    matchCursorOverlay.toFront();
  }
}