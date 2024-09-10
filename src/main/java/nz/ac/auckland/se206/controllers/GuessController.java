package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import javafx.scene.media.AudioClip;

public class GuessController {
  private AudioClip markerSound;

  @FXML
  private ImageView imageView;

  @FXML
  private ImageView imageView1;

  @FXML
  private ImageView imageView2;

  @FXML
  private ImageView clue1;

  @FXML
  private ImageView clue2;

  @FXML
  private ImageView clue3;

  @FXML
  public void initialize() {
    markerSound = new AudioClip(getClass().getResource("/sounds/marker.mp3").toString());
    boolean[] clues = CrimeController.cluesGuessed();

    if (clues[0]) {
      clue1.setImage(new Image("/images/circle.png"));
    }
    if (clues[1]) {
      clue2.setImage(new Image("/images/circle.png"));
    }
    if (clues[2]) {
      clue3.setImage(new Image("/images/circle.png"));
    }
  }

  @FXML
  private void handleHover(MouseEvent event) {
    markerSound.play();
    Image image = new Image(App.class.getResource("/images/scribble.gif").toExternalForm());

    Rectangle clickedRectangle = (Rectangle) event.getSource();
    clickedRectangle.getId();

    if (clickedRectangle.getId().equals("guessRect1")) {
      // Set the image to the ImageView
      imageView.setImage(image);
    } else if (clickedRectangle.getId().equals("guessRect2")) {
      // Set the image to the ImageView
      imageView1.setImage(image);
    } else if (clickedRectangle.getId().equals("guessRect3")) {
      // Set the image to the ImageView
      imageView2.setImage(image);
    }

  }

  @FXML
  private void handleExitHover(MouseEvent event) {
    markerSound.stop();
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    clickedRectangle.getId();

    if (clickedRectangle.getId().equals("guessRect1")) {

      imageView.setImage(null);
    } else if (clickedRectangle.getId().equals("guessRect2")) {

      imageView1.setImage(null);
    } else if (clickedRectangle.getId().equals("guessRect3")) {

      imageView2.setImage(null);
    }
  }

}
