package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.ImageCursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.Navigation;
import nz.ac.auckland.se206.TimerManager;

public class LetterCloseUpController {

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
  private Label infoLabel;
  @FXML
  private Rectangle instructionsBox;
  @FXML
  private Pane instructionsPane;
  @FXML
  private Rectangle envelopeCloseUpRec;
  @FXML
  private Pane mutePane;

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
    
    App.setSounds(sounds);
    App.muteSound();

    Image image = new Image(getClass().getResource("/images/closed-envelope.png").toString());
    // Set the envelope image
    envelopeCloseUp.setImage(image);
    Navigation nav = new Navigation();
    // Set the menu button
    nav.setMenu(menuButton);
    eraseCanvas.setDisable(true);
    // If the letter not been burnt, set instructions box
    if (burnt == false) {
      App.animateText("let's see what's inside the envelope..", infoLabel);
    }
    // Load the clue menu
    App.handleClueMenu(clueMenu);
    // Load the hints box
    App.loadHintsBox(instructionsPane);
    // Load the timer
    App.timer(timerLabel);
    // If the letter has been burnt, set the image to the hidden invitation
    if (burnt == true) {
      Image imageHidden = new Image(getClass().getResource(
          "/images/invitationHidden.png").toString());
      letterOpenedReveal.setImage(imageHidden);
      App.animateText("interesting, I wonder who wrote this...", infoLabel);
      matchBox.setDisable(true);
      envelopeCloseUpRec.setDisable(true);
    }

    // if time runs out
    TimerManager.getInstance().timeRemainingProperty().addListener((obs, oldTime, newTime) -> {
      if (newTime.intValue() == 0) {
        handleTimerExpired();
      }
    });

  }

  /**
   * Handles the clue menu.
   *
   * @param pane the pane to display the clue menu
   * @throws IOException if there is an I/O error
   */
  private void handleTimerExpired() {
    setBackCursor();
    matchSound.stop();
  }

  /**
   * Handles the clue menu.
   *
   * @param pane the pane to display the clue menu
   * @throws IOException if there is an I/O error
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
      // create canvas for erasing the layer with fire cursor
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
    // check if envelope is open
    if (envelopeClicked < 2) {
      return;
    } else {
      // Set the matchbox cursor
      matchBoxClicked = true;
      buttonClickSound.play();
      matchSound.play();
      setMatchboxCursor();
    }

  }

  /**
   * Sets the cursor to the matchbox image.
   */
  private void setMatchboxCursor() {
    // Load custom cursor image
    Image cursorImage = new Image(getClass().getResource("/images/matchstick.png").toString());
    ImageCursor customCursor = new ImageCursor(cursorImage);
    if (envelopeCloseUp.getScene() != null) {
      envelopeCloseUp.getScene().setCursor(customCursor);
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
        System.out.println("inside area");
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
    isErasing = false;

  }

  /**
   * Sets the cursor back to the default cursor.
   */
  @FXML
  private void onBack() throws IOException {
    buttonClickSound.seek(javafx.util.Duration.ZERO); 

    matchSound.stop();
    buttonClickSound.play();
    setBackCursor();

    App.setRoot("crime");
  }

  /**
   * Sets the cursor back to the default cursor.
   */
  private void setBackCursor() {
    Image cursorImage = new Image(getClass().getResource("/images/magnifying.png").toString());
    ImageCursor customCursor = new ImageCursor(cursorImage);
    // Set the cursor back to the default cursor
    if (envelopeCloseUp.getScene() != null) {
      envelopeCloseUp.getScene().setCursor(customCursor);
    }
    matchSound.stop();
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