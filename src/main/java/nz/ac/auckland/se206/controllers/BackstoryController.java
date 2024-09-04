package nz.ac.auckland.se206.controllers;

import java.io.IOException;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaPlayer;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;

public class BackstoryController {

  private MediaPlayer screamPlayer;
  @FXML
  private Button startButton;
  @FXML
  private Pane backstoryPane;

  /** Initialises the backstory scene. */
  @FXML
  public void initialize() {
    // Backstory pane is initially invisible
    backstoryPane.setVisible(false);
    
    // Create a Media object for the scream sound
    Media screamMedia = new Media(getClass().getResource("/sounds/scream.mp3").toString());
    
    // Create a MediaPlayer for the scream sound
    screamPlayer = new MediaPlayer(screamMedia);
    
    // Set an event handler to make the pane visible again when the scream finishes
    screamPlayer.setOnEndOfMedia(() -> backstoryPane.setVisible(true));
    
    // Play the scream audio clip
    screamPlayer.play();
  }

  /** Handles the start button pressed event and switches to the next scene. */
  @FXML
  private void onStart(ActionEvent event) throws ApiProxyException, IOException {

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
            App.setRoot("crime");
          } catch (IOException ioException) {
            ioException.printStackTrace();
          }
        });

    // Start the fade out
    fadeOut.play();
  }
}
