package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.ClueMenu;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.Navigation;
import nz.ac.auckland.se206.TimerManager;

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
  Canvas eraseCanvas;

  @FXML
  private MenuButton menuButton;

  @FXML
  private Label timerLabel;

  @FXML
  private Pane clueMenu;

  private int envelopeClicked = 0;
  private GraphicsContext gc;
  private boolean isErasing = false;
  public static boolean LetterFound = false;
  private static GameStateContext context = new GameStateContext();

  public void initialize() {
    buttonClickSound = new AudioClip(getClass().getResource("/sounds/click.mp3").toString());
    matchSound = new AudioClip(getClass().getResource("/sounds/fire-crackling.wav").toString());
    Image image = new Image(getClass().getResource("/images/closed-envelope.png").toString());
    envelopeCloseUp.setImage(image);
    Navigation nav = new Navigation();
    nav.setMenu(menuButton);
    eraseCanvas.setDisable(true);

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

  }

  @FXML
  public void envelopeClicked(MouseEvent event) {
    buttonClickSound.play();

    if (envelopeClicked == 0) {
      envelopeClicked++;
      letterOpened.setImage(null);
      Image image = new Image(getClass().getResource("/images/open-envelope.png").toString());
      envelopeCloseUp.setImage(image);
      return;
    } else {
      envelopeClicked++;
      Image imageHidden = new Image(getClass().getResource("/images/invitationHidden.jpg").toString());
      letterOpenedReveal.setImage(imageHidden);
      LetterFound = true;

      createcanvas();

    }
  }

  public static boolean isLetterFound() {
    return LetterFound;
  }

  @FXML
  public void HandleMatchBoxClick(MouseEvent event) {
    matchSound.play();
    if (envelopeClicked < 2) {
      return;
    } else {
      buttonClickSound.play();
      setMatchboxCursor();
    }

  }

  private void setMatchboxCursor() {
    // Load custom cursor image
    Image cursorImage = new Image(getClass().getResource("/images/matchstick.png").toString());

    // Create an ImageCursor with the image
    ImageCursor customCursor = new ImageCursor(cursorImage);

    // Set the cursor for the whole scene (or a specific node like the root pane or
    // canvas)
    // Obtain the scene from any node, for example, the envelopeCloseUp
    envelopeCloseUp.getScene().setCursor(customCursor); // Apply to the scene
    // OR if you want to apply it to the canvas only:
    // eraseCanvas.setCursor(customCursor);
  }

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
    eraseCanvas.setOnMouseDragged(this::erase);
  }

  /**
   * Starts erasing when the mouse is pressed.
   */
  private void startErasing(MouseEvent event) {
    isErasing = true;
    erase(event); // Erase the point where the mouse is pressed
  }

  /**
   * Erases the opaque layer to reveal the hidden invitation.
   */
  private void erase(MouseEvent event) {
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

  @FXML
  private void eraseLetter(MouseEvent event) {

    // Set up the canvas for erasing
    // letterOpened.setImage(null);
    // GraphicsContext gc = eraseCanvas.getGraphicsContext2D();
    // gc.setFill(javafx.scene.paint.Color.WHITE); // Start with a solid white cover
    // (or other color)
    // gc.fillRect(0, 0, eraseCanvas.getWidth(), eraseCanvas.getHeight());

    // // Handle the drag event to "erase" parts of the canvas
    // eraseCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::handleErase);
  }

  // private void handleErase(MouseEvent event) {
  // GraphicsContext gc = eraseCanvas.getGraphicsContext2D();

  // // Get the coordinates of the mouse
  // double x = event.getX();
  // double y = event.getY();

  // // Clear a circular area where the mouse is dragged to "reveal" the image
  // gc.clearRect(x - 15, y - 15, 30, 30); // Erase a 30x30 area around the drag
  // point
  // }

  /**
   * Handles the guess button click event.
   *
   * @param event the action event triggered by clicking the guess button
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleGuessClick(ActionEvent event) throws IOException {
    buttonClickSound.play();
    App.setRoot("guess");
    context.handleGuessClick();
  }

  @FXML
  public void handleClueMenu(Pane pane) throws IOException {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/clueMenu.fxml"));
    Pane menuPane = loader.load();

    pane.getChildren().clear();
    pane.getChildren().add(menuPane);

  }

  @FXML
  public void onBack() throws IOException {
    matchSound.stop();
    buttonClickSound.play();
    setBackCursor();

    App.setRoot("crime");
  }

  private void setBackCursor() {
    // Load custom cursor image
    Image cursorImage = new Image(getClass().getResource("/images/magnifying_glass.png").toString());

    // Create an ImageCursor with the image
    ImageCursor customCursor = new ImageCursor(cursorImage);

    // Set the cursor for the whole scene (or a specific node like the root pane or
    // canvas)
    // Obtain the scene from any node, for example, the envelopeCloseUp
    envelopeCloseUp.getScene().setCursor(customCursor); // Apply to the scene
    // OR if you want to apply it to the canvas only:
    // eraseCanvas.setCursor(customCursor);
  }
}
