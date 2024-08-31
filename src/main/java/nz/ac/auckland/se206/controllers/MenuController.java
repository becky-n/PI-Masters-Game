package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.media.AudioClip;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;

/**
 * Controller class for the menu view. Handles user interactions within the menu where the user can
 * start the game.
 */
public class MenuController {
  private AudioClip backgroundMusic;

  /** Initialises the menu scene. */
  @FXML
  public void initialize() {
    backgroundMusic = new AudioClip(getClass().getResource("/sounds/wedding_march.mp3").toString());
    backgroundMusic.play();
  }

  @FXML
  private void onStart(ActionEvent event) throws ApiProxyException, IOException {}
}
