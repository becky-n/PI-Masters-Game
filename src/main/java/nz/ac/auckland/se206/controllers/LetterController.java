package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.InstructionsManager;
import nz.ac.auckland.se206.Navigation;
import nz.ac.auckland.se206.TimerManager;

public class LetterController {

  // Static variables
  public static boolean burnt;
  private static GameStateContext context = new GameStateContext();

  // Instance variables
  private AudioClip buttonClickSound;

  // FXML annotated variables
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
  private ImageView envelope;

  /**
   * Initializes the LetterController. Sets up the timer, menu navigation, chat,
   * and loads the clue menu and hints box.
   * 
   * @throws IOException if there is an I/O error during initialization
   */
  public void initialize() {
    // Play button click sound
    buttonClickSound = new AudioClip(getClass().getResource("/sounds/click.mp3").toString());
    Navigation nav = new Navigation();
    nav.setMenu(menuButton);
    // Load the clue menu
    try {
      handleClueMenu(clueMenu);
    } catch (IOException e) {
      e.printStackTrace();
    }
    // Load the hints box
    try {
      loadHintsBox(instructionsPane);
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
   * Loads the hints box into the provided pane.
   * 
   * @param pane the pane where the hints box will be loaded
   * @throws IOException if there is an I/O error during loading
   */
  private void loadHintsBox(Pane pane) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/instructions.fxml"));
    Pane hintsPane = loader.load();
    pane.getChildren().clear();
    pane.getChildren().add(hintsPane);
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
    // Play the button click sound
    buttonClickSound.play();
    boolean[] suspects = ChatController.suspectsTalkedTo();
    boolean[] clues = CrimeController.cluesGuessed();
    boolean allSuspectsTalkedTo = suspects[0] && suspects[1] && suspects[2];
    boolean atLeastOneClueFound = clues[0] || clues[1] || clues[2];
    // Check if the player has talked to all suspects and found at least one clue
    if (suspects[0] && suspects[1] && suspects[2]) {
      if (clues[0] || clues[1] || clues[2]) {
        context.handleGuessClick();
        App.setRoot("guess");
      }
      // if player hasnt talked to all suspects but found one clue
    } else if (!allSuspectsTalkedTo && atLeastOneClueFound) {
      InstructionsManager.getInstance().updateInstructions(
          "You must talk to all suspects before making a guess.");
      InstructionsManager.getInstance().showInstructions();
      // if player has talked to all suspects but hasnt found a clue
    } else if (!atLeastOneClueFound && allSuspectsTalkedTo) {
      InstructionsManager.getInstance().updateInstructions(
          "You must find at least one clue before making a guess.");
      InstructionsManager.getInstance().showInstructions();
      // if player hasnt talked to all suspects and hasnt found a clue
    } else {
      InstructionsManager.getInstance().updateInstructions(
          "You must talk to all suspects and find at least one clue before making a guess.");
      InstructionsManager.getInstance().showInstructions();
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
