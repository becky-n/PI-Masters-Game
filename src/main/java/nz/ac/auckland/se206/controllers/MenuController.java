package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;

/**
 * Controller class for the menu view. Handles user interactions within the menu
 * where the user can
 * start the game.
 */
public class MenuController {
  private MediaPlayer backgroundMusic;
  private MediaPlayer buttonClickSound;
  private MediaPlayer twinkleSound;

  @FXML
  private Button startButton;
  @FXML
  private Slider volumeSlider;

  /** Initialises the menu scene. */
  @FXML
  public void initialize() {
    Media backgroundMusicMedia = new Media(getClass().getResource("/sounds/wedding_march.mp3").toString());
    backgroundMusic = new MediaPlayer(backgroundMusicMedia);

    Media buttonClickMedia = new Media(getClass().getResource("/sounds/click.mp3").toString());
    buttonClickSound = new MediaPlayer(buttonClickMedia);

    Media twinkleMedia = new Media(getClass().getResource("/sounds/twinkle.mp3").toString());
    twinkleSound = new MediaPlayer(twinkleMedia);

    // Loop the background music
    backgroundMusic.setOnEndOfMedia(() -> backgroundMusic.seek(Duration.ZERO));
    backgroundMusic.play();

    // Setup slider to control the volume of all media
    double initialVolume = 1; // Example initial volume
    backgroundMusic.setVolume(initialVolume);
    buttonClickSound.setVolume(initialVolume);
    twinkleSound.setVolume(initialVolume);

    // Sync volume slider with the volume of all media players
    volumeSlider.setValue(initialVolume * 100);
    volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
      double volume = newValue.doubleValue() / 100;
      backgroundMusic.setVolume(volume);
      buttonClickSound.setVolume(volume);
      twinkleSound.setVolume(volume);

      // Save the volume for future scenes
      App.setVolume(volume);
    });
  }

  /** Handles the button hover event and plays a sound effect. */
  @FXML
  private void buttonHover() {
    twinkleSound.seek(Duration.ZERO); // Reset to the beginning of the sound
    twinkleSound.play();
  }

  /** Handles the close button pressed event and exits the application. */
  @FXML
  private void handleClose() {
    Platform.exit();
  }

  /** Handles the start button pressed event and switches to the next scene. */
  @FXML
  private void onStart(ActionEvent event) throws ApiProxyException, IOException {
    backgroundMusic.stop();
    buttonClickSound.play();

    App.fadeScenes("backstory");
  }
}
