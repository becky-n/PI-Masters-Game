package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import nz.ac.auckland.se206.controllers.CrimeController;

public class ClueMenuController {

  @FXML
  private ImageView clue1;

  @FXML
  private ImageView clue2;

  @FXML
  private ImageView clue3;

  public void initialize() {
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

}
