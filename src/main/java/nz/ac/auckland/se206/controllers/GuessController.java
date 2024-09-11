package nz.ac.auckland.se206.controllers;

import java.io.IOException;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.TimerManager;
import javafx.scene.media.AudioClip;

public class GuessController {

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
  private Label timerLabel;

  @FXML
  private Pane tabletPane;

  @FXML
  private Label infoLabel;

  private AudioClip markerSound;
  private AudioClip typingSound;
  private AudioClip screenOnSound;
  private Boolean isTabletOpen;
  private String suspect = "";

  @FXML
  public void initialize() {

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

    TimerManager timerManager = TimerManager.getInstance();
    animateText("Click on who stole the ring");

    // Bind the timerLabel to the timeRemaining property
    timerLabel.textProperty().bind(
        Bindings.createStringBinding(() -> String.format("%02d:%02d",
            timerManager.getTimeRemaining() / 60,
            timerManager.getTimeRemaining() % 60),
            timerManager.timeRemainingProperty()));

    boolean[] clues = CrimeController.cluesGuessed();

    // Show the clues if the clues have been guessed
    if (clues[0]) {
      clue1.setImage(new Image("/images/circle.png"));
    }
    if (clues[1]) {
      clue2.setImage(new Image("/images/circle.png"));
    }
    if (clues[2]) {
      clue3.setImage(new Image("/images/circle.png"));
    }
  }

 @FXML
private void handleGuessClick(MouseEvent event) throws IOException {
    if (isTabletOpen) {
        return;
    }

    Rectangle clickedRectangle = (Rectangle) event.getSource();

    // Determine which rectangle was clicked
    if (clickedRectangle.getId().equals("guessRect1")) {
        this.suspect = "Andrea";
        circleAndrea.setVisible(true);
    } else if (clickedRectangle.getId().equals("guessRect2")) {
        this.suspect = "Jesin";
        circleJesin.setVisible(true);
    } else if (clickedRectangle.getId().equals("guessRect3")) {
        this.suspect = "Gerald";
        circleGerald.setVisible(true);
    }

    screenOnSound.play();

    App.openTablet(suspect, tabletPane); 
    isTabletOpen = true;
}


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
      imageView.setImage(image);
    } else if (clickedRectangle.getId().equals("guessRect2")) {
      // Set the image to the ImageView
      imageView1.setImage(image);
    } else if (clickedRectangle.getId().equals("guessRect3")) {
      // Set the image to the ImageView
      imageView2.setImage(image);
    }

  }

  @FXML
  private void handleExitHover(MouseEvent event) {
    markerSound.stop();
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    clickedRectangle.getId();

    if (clickedRectangle.getId().equals("guessRect1")) {

      imageView.setImage(null);
    } else if (clickedRectangle.getId().equals("guessRect2")) {

      imageView1.setImage(null);
    } else if (clickedRectangle.getId().equals("guessRect3")) {

      imageView2.setImage(null);
    }
  }

  @FXML
  private void handleIpadClick(MouseEvent event) throws IOException {
    if (isTabletOpen) {
      return;
    }

    animateText("Choose a suspect first");
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

}
