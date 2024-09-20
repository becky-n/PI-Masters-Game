package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.TimerManager;

public class GuessController {
  public static boolean play = false;
  // this field is used to determine if the game is in the guessing state
  public static boolean guess1 = false;

  /**
   * Returns whether the game is in the guessing state.
   *
   * @return true if the game is in the guessing state, false otherwise
   */
  public static boolean inGuessingState() {
    return guess1;
  }

  @FXML
  private ImageView imageView;
  @FXML
  private ImageView imageView1;
  @FXML
  private ImageView imageView2;
  @FXML
  private ImageView clue1;
  @FXML
  private ImageView clue2;
  @FXML
  private ImageView clue3;
  @FXML
  private ImageView circleAndrea;
  @FXML
  private ImageView circleJesin;
  @FXML
  private ImageView circleGerald;
  @FXML
  private ImageView andreaMad;
  @FXML
  private ImageView jesinMad;
  @FXML
  private ImageView geraldMad;
  @FXML
  private Label timerLabel;
  @FXML
  private Pane tabletPane;
  @FXML
  private Label infoLabel;
  @FXML
  private Button playAgainButton;
  @FXML
  private Button close;

  private AudioClip markerSound;
  private AudioClip typingSound;
  private AudioClip screenOnSound;
  private AudioClip sadMusic;
  private AudioClip happyMusic;

  private Boolean isTabletOpen;
  private String suspect = "";

  /**
   * Sets the game to the guessing state.
   *
   * @param guess true if the game is in the guessing state, false otherwise
   */
  @FXML
  public void initialize() {
    sadMusic = new AudioClip(getClass().getResource("/sounds/sad-music.mp3").toString());
    happyMusic = new AudioClip(getClass().getResource("/sounds/happy.mp3").toString());

    guess1 = true;

    playAgainButton.setVisible(false);
    close.setVisible(false);

    // reset for each new game
    isTabletOpen = false;

    // Initialize the sounds
    markerSound = new AudioClip(getClass().getResource("/sounds/marker.mp3").toString());
    typingSound = new AudioClip(getClass().getResource("/sounds/typing.mp3").toString());
    screenOnSound = new AudioClip(getClass().getResource("/sounds/screen-on.mp3").toString());

    // make all circles invisible
    circleAndrea.setVisible(false);
    circleJesin.setVisible(false);
    circleGerald.setVisible(false);

    
    animateText("Click on who stole the ring");

    App.timer(timerLabel);

    boolean[] clues = CrimeController.cluesGuessed();
    boolean isBurnt = LetterCloseUpController.burnt;

    // Show the clues if the clues have been guessed
    if (clues[0] && LockController.isBoxUnlocked()) {
      clue1.setImage(new Image("/images/taped_ring_clue.png"));
    }
    if (clues[1] && WindowController.fabricFound()) {
      clue2.setImage(new Image("/images/taped_fabric_clue.png"));
    }
    if (clues[2] && isBurnt) {
      clue3.setImage(new Image("/images/taped_letter_clue.png"));
    }
  }

  /**
   * Handles the guess button click event.
   *
   * @param event the action event triggered by clicking the guess button
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleGuessClick(MouseEvent event) throws IOException {
    // check if tablet is open
    if (isTabletOpen) {
      return;
    }

    Rectangle clickedRectangle = (Rectangle) event.getSource();

    // check what player selected
    if (clickedRectangle.getId().equals("guessRect3")) {
      this.suspect = "Gerald";
      circleGerald.setVisible(true);

      screenOnSound.play();

      App.openTablet(suspect, tabletPane, this);
      isTabletOpen = true;

      return;
    }

    // if player didn't select gerald
    if (clickedRectangle.getId().equals("guessRect1")) {
      this.suspect = "Andrea";
      circleAndrea.setVisible(true);
    } else if (clickedRectangle.getId().equals("guessRect2")) {
      this.suspect = "Jesin";
      circleJesin.setVisible(true);
    }

    App.openTimesUp(suspect + " was not the thief!");

  }

  /**
   * Handles the key press event.
   *
   * @param event the key event triggered by pressing a key
   */
  @FXML
  private void handleHover(MouseEvent event) {
    if (isTabletOpen) {
      return;
    }

    markerSound.play();
    Image image = new Image(App.class.getResource("/images/scribble.gif").toExternalForm());

    Rectangle clickedRectangle = (Rectangle) event.getSource();
    clickedRectangle.getId();

    if (clickedRectangle.getId().equals("guessRect1")) {
      // Set the image to the ImageView
      Image madAndrea = new Image(App.class.getResource("/images/madAndrea.png").toExternalForm());
      imageView.setImage(image);
      andreaMad.setImage(madAndrea);
    } else if (clickedRectangle.getId().equals("guessRect2")) {
      // Set the image to the ImageView
      Image madJesin = new Image(App.class.getResource("/images/madJesin.png").toExternalForm());
      imageView1.setImage(image);
      jesinMad.setImage(madJesin);
    } else if (clickedRectangle.getId().equals("guessRect3")) {
      // Set the image to the ImageView
      Image madGerald = new Image(App.class.getResource("/images/madGerald.png").toExternalForm());
      imageView2.setImage(image);
      geraldMad.setImage(madGerald);
    }

  }

  /**
   * Handles the key press event.
   *
   * @param event the key event triggered by pressing a key
   */
  @FXML
  private void handlePlayAgain() throws IOException {
    happyMusic.stop();
    sadMusic.stop();
    play = true;
    System.out.println("play again");
    CrimeController.resetClues(); // Reset clues
    ChatController.resetSuspects(); // Reset suspects
    LockController.resetLock();
    WindowController.resetFabric();
    LetterCloseUpController.resetLetter();
    TimerManager.getInstance().reset(300);
    guess1 = false;
    App.setRoot("menu");

  }

  /**
   * Handles the key press event.
   *
   * @param event the key event triggered by pressing a key
   */
  @FXML
  private void handleExitHover(MouseEvent event) {
    // stop the marker sound
    markerSound.stop();
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    clickedRectangle.getId();

    // remove the image
    if (clickedRectangle.getId().equals("guessRect1")) {
      // Set the image to the original andrea
      imageView.setImage(null);
      andreaMad.setImage(null);
    } else if (clickedRectangle.getId().equals("guessRect2")) {
      // Set the image to the original jesin
      imageView1.setImage(null);
      jesinMad.setImage(null);
    } else if (clickedRectangle.getId().equals("guessRect3")) {
      // Set the image to the original gerald
      imageView2.setImage(null);
      geraldMad.setImage(null);
    }
  }

  /**
   * Handles the key press event.
   *
   * @param event the key event triggered by pressing a key
   */
  @FXML
  private void handleIpadClick(MouseEvent event) throws IOException {
    if (isTabletOpen) {
      return;
    }

    animateText("Choose a suspect first");
  }

  @FXML
  private void handleClose() {
    Platform.exit();
  }

  /**
   * Animates the text in the info label with an underscore following each
   * character.
   */
  private void animateText(String str) {
    typingSound.play();
    final IntegerProperty i = new SimpleIntegerProperty(0);
    Timeline timeline = new Timeline();
    KeyFrame keyFrame = new KeyFrame(
        Duration.seconds(0.07), // Adjusted for smoother animation
        event -> {
          if (i.get() > str.length()) {
            timeline.stop();
            typingSound.stop();
          } else {
            // Append underscore to the current substring
            String animatedText = str.substring(0, i.get()) + "_";
            infoLabel.setText(animatedText);
            i.set(i.get() + 1);
          }
        });
    timeline.getKeyFrames().add(keyFrame);
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
  }

  /**
   * Toggles the visibility of the play again button.
   *
   * @param toggle true if the button should be visible, false otherwise
   */
  public void togglePlayAgainButton(boolean toggle) {
    playAgainButton.setVisible(toggle);
    close.setVisible(toggle);

  }

}
