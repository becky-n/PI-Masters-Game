package nz.ac.auckland.se206.controllers;

import java.io.IOException;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.TimerManager;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class BackstoryController {

  @FXML
  private Button startButton;
  @FXML
  private Pane backstoryPane;
  @FXML
  private ImageView screamImg;
  @FXML
  private Label backstoryLabel;
  @FXML
  private Label timerLabel;

  private MediaPlayer screamPlayer;
  private AudioClip backgroundMusic;
  private AudioClip buttonClickSound;
  private AudioClip twinkleSound;

  /** The string to animate. */
  private final String str = "Thank goodness you're with PI Masters! "
      + "My ring is missing and the wedding is in 5 minutes! "
      + "I'm suspicious of Gerald, Jesin, and Andrea... "
      + "Can you talk with them and gather clues before it's too late?"; // string to animate

  /** Initialises the backstory scene. */
  @FXML
  public void initialize() {
    // Backstory pane is initially invisible
    backstoryPane.setVisible(false);

    // Initialize media resources
    Media screamMedia = new Media(getClass().getResource("/sounds/scream.mp3").toString());
    TimerManager timerManager = TimerManager.getInstance();

    backgroundMusic = new AudioClip(getClass().getResource("/sounds/mystery_music.mp3").toString());
    buttonClickSound = new AudioClip(getClass().getResource("/sounds/click.mp3").toString());
    twinkleSound = new AudioClip(getClass().getResource("/sounds/twinkle.mp3").toString());
    screamPlayer = new MediaPlayer(screamMedia);

    // Bind the timerLabel to the timeRemaining property
    timerLabel.textProperty().bind(
        Bindings.createStringBinding(() -> String.format("%02d:%02d",
            timerManager.getTimeRemaining() / 60,
            timerManager.getTimeRemaining() % 60),
            timerManager.timeRemainingProperty()));

    // Start the timer if it's the first scene
    if (!timerManager.isRunning()) {
      timerManager.start(300);
    }

    // Start the scream sound and apply the shake effect
    screamPlayer.play();
    applyShakeEffect();
  }

  /** Adds a shake effect to the screamImg during the scream sound */
  private void applyShakeEffect() {
    // Create a Timeline to shake the ImageView
    Timeline shakeTimeline = new Timeline();

    KeyFrame keyFrame = new KeyFrame(
        Duration.millis(50), // Change position every 50 milliseconds
        event -> {
          double xOffset = (Math.random() - 0.5) * 20; // Random X offset (-10 to 10)
          double yOffset = (Math.random() - 0.5) * 20; // Random Y offset (-10 to 10)
          screamImg.setTranslateX(xOffset);
          screamImg.setTranslateY(yOffset);
        });

    // Add the keyframe to the timeline
    shakeTimeline.getKeyFrames().add(keyFrame);
    shakeTimeline.setCycleCount(Animation.INDEFINITE); // Make it indefinite until stopped

    // Start the shaking when the scream starts
    shakeTimeline.play();

    // Stop the shake effect and initiate backstory and background music
    screamPlayer.setOnEndOfMedia(() -> {
      shakeTimeline.stop();
      backstoryPane.setVisible(true);
      backgroundMusic.play();
      animateText();
    });
  }

  /** Animates the text in the motive label. */
  private void animateText() {
    final IntegerProperty i = new SimpleIntegerProperty(0);
    Timeline timeline = new Timeline();
    KeyFrame keyFrame = new KeyFrame(
        Duration.seconds(0.015), // Adjusted for smoother animation
        event -> {
          if (i.get() > str.length()) {
            timeline.stop();
          } else {
            backstoryLabel.setText(str.substring(0, i.get()));
            i.set(i.get() + 1);
          }
        });
    timeline.getKeyFrames().add(keyFrame);
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
  }

  /** Handles the button hover event and plays a sound effect. */
  @FXML
  private void buttonHover() {
    twinkleSound.play();
  }

  /** Handles the start button pressed event and switches to the next scene. */
  @FXML
  private void onStart(ActionEvent event) throws ApiProxyException, IOException {
    backgroundMusic.stop();
    buttonClickSound.play();
    App.fadeScenes("crime");
  }
}
