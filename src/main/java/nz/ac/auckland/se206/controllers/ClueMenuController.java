package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * The ClueMenuController class is responsible for managing the clue menu in the
 * application.
 * 
 */
public class ClueMenuController {

  @FXML
  private ImageView clue1;
  @FXML
  private ImageView clue2;
  @FXML
  private ImageView clue3;

  /**
   * Initialize the clues in the clue menu
   */
  public void initialize() {
    boolean[] clues = CrimeController.cluesGuessed();
    boolean isBurnt = LetterCloseUpController.burnt;

    // if clues not found don't show an image
    if (!clues[0]) {
      clue1.setImage(null);
    }
    if (!clues[1]) {
      clue2.setImage(null);
    }
    if (!clues[2]) {
      clue3.setImage(null);
    }

    // show clue image if clue is found
    if (clues[0] && LockController.isBoxUnlocked()) {
      clue1.setImage(new Image("/images/ring_clue.png"));
    }
    if (clues[1] && WindowController.fabricFound()) {
      clue2.setImage(new Image("/images/cloth_clue.png"));
    }
    if (clues[2] && isBurnt) {
      clue3.setImage(new Image("/images/letter_clue.png"));
    }
  }

}
