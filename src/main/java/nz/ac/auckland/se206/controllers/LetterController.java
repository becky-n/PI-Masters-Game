package nz.ac.auckland.se206.controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import nz.ac.auckland.se206.App;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;

public class LetterController {
  private AudioClip buttonClickSound;

  @FXML
  private ImageView envelope;


  public void initialize() {
    buttonClickSound = new AudioClip(getClass().getResource("/sounds/click.mp3").toString());
  }

  @FXML
  public void handleEnvelopeClick(MouseEvent event) {
    buttonClickSound.play();
    try {
      App.setRoot("letterCloseUp");
    } catch (IOException e) {
      e.printStackTrace();
    }
    
  }

}
