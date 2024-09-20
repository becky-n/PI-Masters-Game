package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.Navigation;
import nz.ac.auckland.se206.TimerManager;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.image.ImageView;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.IntegerProperty;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.animation.Animation;

public class LetterController {
  private AudioClip buttonClickSound;

  @FXML
  private MenuButton menuButton;

  @FXML
  private Label timerLabel;

  @FXML
  private Pane clueMenu;

  @FXML
  private Label infoLabel;

  @FXML
  private ImageView envelope;
  public static boolean burnt;
  private static GameStateContext context = new GameStateContext();

  /**
   * Initializes the LetterController. Sets up the timer, menu navigation, chat,
   * and loads the clue menu and hints box.
   * 
   * @throws IOException if there is an I/O error during initialization
   */
  public void initialize() {

    buttonClickSound = new AudioClip(getClass().getResource("/sounds/click.mp3").toString());
    Navigation nav = new Navigation();
    nav.setMenu(menuButton);

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
    animateText("Interesting, I wonder what this envelope contains...");
  }

  /**
   * Handles the envelope click event.
   *
   * @param event the mouse event triggered by clicking the envelope
   * @throws IOException if there is an I/O error
   */
  @FXML
  public void handleEnvelopeClick(MouseEvent event) {
    buttonClickSound.play();
    try {
      App.setRoot("letterCloseUp");
    } catch (IOException e) {
      e.printStackTrace();
    }

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
    boolean[] suspects = ChatController.suspectsTalkedTo();
    boolean[] clues = CrimeController.cluesGuessed();
    // If all suspects have been talked to and at least one clue has been guessed
    if (suspects[0] && suspects[1] && suspects[2]) {
      if (clues[0] || clues[1] || clues[2]) {
        context.handleGuessClick();
        App.setRoot("guess");
      }

    }
  }

  /**
   * Loads the clue menu into the specified pane.
   * 
   * @param pane the pane to which the clue menu should be added
   * @throws IOException if there is an I/O error during loading the clue menu
   */
  @FXML
  public void handleClueMenu(Pane pane) throws IOException {
    // Load the clue menu
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/clueMenu.fxml"));
    Pane menuPane = loader.load();
    pane.getChildren().clear();
    pane.getChildren().add(menuPane);

  }

  /**
   * Handles the back button click event.
   * 
   * @throws IOException if there is an I/O error
   */
  @FXML
  public void onBack() throws IOException {
    // Play button click sound
    buttonClickSound.play();
    App.setRoot("crime");
  }

  /**
   * Animates the text in the info label.
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
