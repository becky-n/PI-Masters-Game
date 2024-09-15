package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.Navigation;
import nz.ac.auckland.se206.TimerManager;

public class LetterCloseUpController {
  private AudioClip buttonClickSound;

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
  private static GameStateContext context = new GameStateContext();

  public void initialize() {
    buttonClickSound = new AudioClip(getClass().getResource("/sounds/click.mp3").toString());
    Image image = new Image(getClass().getResource("/images/closed-envelope.png").toString());
    envelopeCloseUp.setImage(image);
    Navigation nav = new Navigation();
    nav.setMenu(menuButton);

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

    if (envelopeClicked % 2 == 0) {
      envelopeClicked++;
      letterOpened.setImage(null);
      Image image = new Image(getClass().getResource("/images/open-envelope.png").toString());
      envelopeCloseUp.setImage(image);
      return;
    } else {
      envelopeClicked++;
      Image imageHidden = new Image(getClass().getResource("/images/invitationHidden.jpg").toString());
      letterOpenedReveal.setImage(imageHidden);
      Image image = new Image(getClass().getResource("/images/invitation.png").toString());
      letterOpened.setImage(image);

    }
  }

  @FXML
  private void eraseLetter(MouseEvent event) {
    // Set up the canvas for erasing
    letterOpened.setImage(null);
    GraphicsContext gc = eraseCanvas.getGraphicsContext2D();
    gc.setFill(javafx.scene.paint.Color.WHITE); // Start with a solid white cover (or other color)
    gc.fillRect(0, 0, eraseCanvas.getWidth(), eraseCanvas.getHeight());

    // Handle the drag event to "erase" parts of the canvas
    eraseCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::handleErase);
  }

  private void handleErase(MouseEvent event) {
    GraphicsContext gc = eraseCanvas.getGraphicsContext2D();

    // Get the coordinates of the mouse
    double x = event.getX();
    double y = event.getY();

    // Clear a circular area where the mouse is dragged to "reveal" the image
    gc.clearRect(x - 15, y - 15, 30, 30); // Erase a 30x30 area around the drag point
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
    App.setRoot("guess");
    context.handleGuessClick();
  }

  @FXML
  public static void handleClueMenu(Pane pane) throws IOException {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/clueMenu.fxml"));
    Pane menuPane = loader.load();

    pane.getChildren().clear();
    pane.getChildren().add(menuPane);
  }

  @FXML
  public void onBack() throws IOException {
    buttonClickSound.play();
    App.setRoot("crime");
  }
}
