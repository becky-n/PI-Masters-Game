package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.media.AudioClip;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.TimerManager;

/**
 * Controller class for the menu view. Handles user interactions within the menu
 * where the user can
 * start the game.
 */
public class TimesUpController {
  private AudioClip backgroundMusic;
  private AudioClip buttonClickSound;
  private AudioClip twinkleSound;

  /** Initialises the menu scene. */
  @FXML
  public void initialize() {
    backgroundMusic = new AudioClip(getClass().getResource("/sounds/sad-music.mp3").toString());
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
  private void handlePlayAgain() throws IOException {
    backgroundMusic.stop();
    buttonClickSound.play();

    System.out.println("play again");
    CrimeController.resetClues(); // Reset clues
    ChatController.resetSuspects(); // Reset suspects
    LockController.resetLock();
    WindowController.resetFabric();
    TimerManager.getInstance().reset(300);
    GuessController.guess = false;
    App.setRoot("menu");

  }
}
