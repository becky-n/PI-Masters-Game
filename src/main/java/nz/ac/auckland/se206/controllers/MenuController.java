package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.media.AudioClip;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;

/**
 * Controller class for the menu view. Handles user interactions within the menu
 * where the user can
 * start the game.
 */
public class MenuController {
  private AudioClip backgroundMusic;
  private AudioClip buttonClickSound;

  @FXML
  private Button startButton;

  /** Initialises the menu scene. */
  @FXML
  public void initialize() {
    backgroundMusic = new AudioClip(getClass().getResource("/sounds/wedding_march.mp3").toString());
    buttonClickSound = new AudioClip(getClass().getResource("/sounds/click.mp3").toString());
    backgroundMusic.play();
  }

  /** Handles the start button pressed event and switches to the next scene. */
  @FXML
  private void onStart(ActionEvent event) throws ApiProxyException, IOException {
    backgroundMusic.stop();
    buttonClickSound.play();

    // Get the root of the current scene
    Parent root = startButton.getScene().getRoot();

    // Create a fade transition
    FadeTransition fadeOut = new FadeTransition();
    fadeOut.setDuration(javafx.util.Duration.millis(500)); // Set duration to 1 second
    fadeOut.setNode(root);
    fadeOut.setFromValue(1.0);
    fadeOut.setToValue(0.0);

    // When the fade out finishes, switch to the new scene
    fadeOut.setOnFinished(
        e -> {
          try {
            App.setRoot("backstory");
          } catch (IOException ioException) {
            ioException.printStackTrace();
          }
        });

    // Start the fade out
    fadeOut.play();
  }
}
