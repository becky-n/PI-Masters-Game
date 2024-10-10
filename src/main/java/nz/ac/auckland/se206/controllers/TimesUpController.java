package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.TimerManager;

/**
 * Controller class for the menu view. Handles user interactions within the menu
 * where the user can
 * start the game.
 */
public class TimesUpController {
  private MediaPlayer backgroundMusic;
  private MediaPlayer buttonClickSound;
  private MediaPlayer twinkleSound;

  @FXML
  private Label infoLabel;
  @FXML
  private Pane mutePane;

  /**
   * Initialises the menu scene by setting up the background music and sound.
   * 
   * @throws IOException if the media file is not found
   */
  @FXML
  public void initialize() throws IOException {

    Media backgroundMusicMedia = new Media(
        getClass().getResource("/sounds/sad-music.mp3").toString());
    Media buttonClickMedia = new Media(
        getClass().getResource("/sounds/click.mp3").toString());
    Media twinkleMedia = new Media(
        getClass().getResource("/sounds/twinkle.mp3").toString());

    backgroundMusic = new MediaPlayer(backgroundMusicMedia);
    buttonClickSound = new MediaPlayer(buttonClickMedia);
    twinkleSound = new MediaPlayer(twinkleMedia);

    backgroundMusic.play();
    // Loop the background music
    backgroundMusic.setOnEndOfMedia(() -> backgroundMusic.seek(javafx.util.Duration.ZERO));

    // create array of sounds and store
    App.handleMute(mutePane);
    ArrayList<MediaPlayer> sounds = new ArrayList<MediaPlayer>();
    sounds.add(buttonClickSound);
    sounds.add(twinkleSound);
    sounds.add(backgroundMusic);

    App.setSounds(sounds);
    App.muteSound();
  }

  /** Handles the button hover event and plays a sound effect. */
  @FXML
  private void buttonHover() {
    twinkleSound.play();
  }

  @FXML
  private void handleClose() {
    Platform.exit();
  }

  /** Handles the start button pressed event and switches to the next scene. */
  @FXML
  private void handlePlayAgain() throws IOException {
    backgroundMusic.stop();
    buttonClickSound.play();
    // Reset game state
    System.out.println("play again");
    CrimeController.resetClues(); // Reset clues
    ChatController.resetSuspects(); // Reset suspects
    LockController.resetLock();
    WindowController.resetFabric();
    LetterCloseUpController.resetLetter();
    TimerManager.getInstance().reset(300);
    GuessController.guess1 = false;
    App.setRoot("menu");
  }

  /**
   * Sets the text of the info label.
   * 
   * @param text the text to set
   */
  public void setInfoLabel(String text) {
    infoLabel.setText(text);
    System.out.println(text);
  }
}
