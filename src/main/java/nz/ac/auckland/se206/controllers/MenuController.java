package nz.ac.auckland.se206.controllers;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
  private AudioClip twinkleSound;

  @FXML
  private Button startButton;

  /** Initialises the menu scene. */
  @FXML
  public void initialize() {
    backgroundMusic = new AudioClip(getClass().getResource("/sounds/wedding_march.mp3").toString());
    buttonClickSound = new AudioClip(getClass().getResource("/sounds/click.mp3").toString());
    twinkleSound = new AudioClip(getClass().getResource("/sounds/twinkle.mp3").toString());
    backgroundMusic.play();
  }

  /** Handles the button hover event and plays a sound effect. */
  @FXML
  private void buttonHover() {
    twinkleSound.play();
  }

  @FXML
  private void handleClose(){
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
