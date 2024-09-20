package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.media.AudioClip;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.Navigation;
import nz.ac.auckland.se206.TimerManager;
import javafx.beans.property.IntegerProperty;
import javafx.animation.Animation;

public class LetterCloseUpController {
  private AudioClip buttonClickSound;

  private AudioClip matchSound;

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
  private Rectangle envelopeCloseUpRec;

  private int envelopeClicked = 0;
  private GraphicsContext gc;
  private boolean displayed = false;
  private boolean isErasing = false;
  public static boolean burnt = false;
  private boolean matchBoxClicked = false;
  private static GameStateContext context = new GameStateContext();

  /**
   * Resets the letter to its original state.
   */
  public static void resetLetter() {
    burnt = false;
  }

  /**
   * Initializes the LetterCloseUpController. Sets up the timer, menu navigation,
   * chat,
   * and loads the clue menu and hints box.
   */
  public void initialize() {
    buttonClickSound = new AudioClip(getClass().getResource("/sounds/click.mp3").toString());
    matchSound = new AudioClip(getClass().getResource("/sounds/fire-crackling.wav").toString());
    Image image = new Image(getClass().getResource("/images/closed-envelope.png").toString());
    envelopeCloseUp.setImage(image);
    Navigation nav = new Navigation();
    nav.setMenu(menuButton);
    eraseCanvas.setDisable(true);

    if (burnt == false) {
      animateText("let's see what's inside the envelope..");
    }

    try {
      handleClueMenu(clueMenu);
    } catch (IOException e) {
      e.printStackTrace();
    }

    TimerManager timerManager = TimerManager.getInstance();

    // Bind the timerLabel to the timeRemaining property
    timerLabel
        .textProperty()
        .bind(
            Bindings.createStringBinding(
                () -> String.format(
                    "%02d:%02d",
                    timerManager.getTimeRemaining() / 60, timerManager.getTimeRemaining() % 60),
                timerManager.timeRemainingProperty()));
    if (burnt == true) {
      Image imageHidden = new Image(getClass().getResource(
        "/images/invitationHidden.png").toString());
      letterOpenedReveal.setImage(imageHidden);
      animateText("interesting, I wonder who wrote this...");
      matchBox.setDisable(true);
      envelopeCloseUpRec.setDisable(true);
    }

    // if time runs out
    timerManager.timeRemainingProperty().addListener((obs, oldTime, newTime) -> {
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
    buttonClickSound.play();

    if (envelopeClicked == 0) {
      envelopeClicked++;
      letterOpened.setImage(null);
      Image image = new Image(getClass().getResource(
          "/images/open-envelope.png").toString());
      envelopeCloseUp.setImage(image);
      return;
    } else {
      envelopeClicked++;
      Image imageHidden = new Image(getClass().getResource(
          "/images/invitationHidden.png").toString());
      letterOpenedReveal.setImage(imageHidden);
      animateText("I wonder what will happen if you burn the paper...");
      if (burnt == true) {
        eraseCanvas.setDisable(false);
        return;
      }

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

    if (envelopeClicked < 2) {
      return;
    } else {
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
        animateText("interesting, I wonder who wrote this...");
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
   * Handles the guess button click event.
   *
   * @param event the action event triggered by clicking the guess button
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleGuessClick(ActionEvent event) throws IOException {
    buttonClickSound.play();
    setBackCursor();
    boolean[] suspects = ChatController.suspectsTalkedTo();
    boolean[] clues = CrimeController.cluesGuessed();
    if (suspects[0] && suspects[1] && suspects[2]) {
      if (clues[0] || clues[1] || clues[2]) {
        context.handleGuessClick();
        App.setRoot("guess");
      }

    }
  }

  /**
   * Sets the cursor back to the default cursor.
   */
  @FXML
  public void handleClueMenu(Pane pane) throws IOException {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/clueMenu.fxml"));
    Pane menuPane = loader.load();

    pane.getChildren().clear();
    pane.getChildren().add(menuPane);

  }

  /**
   * Sets the cursor back to the default cursor.
   */
  @FXML
  public void onBack() throws IOException {
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
   * Animates the text to display one character at a time.
   *
   * @param str the text to animate
   */
  private void animateText(String str) {
    final IntegerProperty i = new SimpleIntegerProperty(0);
    Timeline timeline = new Timeline();
    KeyFrame keyFrame = new KeyFrame(
        Duration.seconds(0.015), // Adjusted for smoother animation
        event -> {
          if (i.get() > str.length()) {
            timeline.stop();
          } else {
            infoLabel.setText(str.substring(0, i.get()));
            i.set(i.get() + 1);
          }
        });
    timeline.getKeyFrames().add(keyFrame);
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
  }
}
