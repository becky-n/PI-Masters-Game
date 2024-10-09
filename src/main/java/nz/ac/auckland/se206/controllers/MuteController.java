package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.App;

/**
 * Controller class for the Mute scene that handles the mute button.
 */
public class MuteController {
  @FXML
  private ImageView mute;

  /**
   * Initializes the mute button image based on current state.
   */
  @FXML
  private void initialize() {
    // set the image of the mute button
    if (App.getVolume() == 0) {
      mute.setImage(new Image("/images/mute.png"));
    } else {
      mute.setImage(new Image("/images/unmute.png"));
    }
  }

  /**
   * Handles the button hover event and plays a sound effect.
   */
  @FXML
  private void onMuteHover() {
    mute.setOpacity(0.8);
  }

  /**
   * Handles the button hover event and plays a sound effect.
   */
  @FXML
  private void offMuteHover() {
    mute.setOpacity(0.5);
  }

  /**
   * Handles the mute button pressed event and toggles the volume of all media.
   */
  @FXML
  private void onMute() {
    // if current image is unmmute set image to mute
    if (mute.getImage().getUrl().contains("unmute")) {
      mute.setImage(new Image("/images/mute.png"));
      App.setVolume(0);
      App.muteSound();
    } else {
      // if current image is mute set image to unmute
      mute.setImage(new Image("/images/unmute.png"));
      App.setVolume(1);
      App.muteSound();
    }
  }
}