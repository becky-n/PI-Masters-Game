package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.InstructionsManager;

/*
 * Controller class for the Instructions scene.
 */
public class InstructionsController {

  @FXML
  private Rectangle textArea;

  @FXML
  private Label instructionsLabel;
  @FXML
  private Circle circle;

  private MediaPlayer buttonClickSound;

  /**
   * Initializes the controller.
   */
  public void initialize() {

    // Any required initialization code can be placed here
    Media buttonClickMedia = new Media(getClass().getResource("/sounds/click.mp3").toString());
    buttonClickSound = new MediaPlayer(buttonClickMedia);

    // add sound to array
    App.addSound(buttonClickSound);
    App.muteSound();

    // hide the text area
    textArea.setVisible(false);
    instructionsLabel.setVisible(false);
    // Bind the hintsTextArea text property to the hints managed in HintsManager
    InstructionsManager.getInstance().setInstructionsController(this);
    instructionsLabel.setText(InstructionsManager.getInstance().getCurrentInstructions());

  }

  /**
   * Updates the instructions text area with the given instructions.
   * 
   * @param instructions the instructions to display
   */
  public void updateInstructions(String instructions) {
    // Update the text whenever it changes in the HintsManager
    instructionsLabel.setText(instructions);

  }

  /**
   * Handles the hints button being clicked.
   */
  @FXML
  public void hintClicked() {
    buttonClickSound.seek(javafx.util.Duration.ZERO);
    buttonClickSound.play();
    // Show the hints text area when the hints button is clicked
    if (textArea.isVisible()) {
      textArea.setVisible(false);
      instructionsLabel.setVisible(false);
    } else {
      textArea.setVisible(true);
      instructionsLabel.setVisible(true);
    }
  }

  @FXML
  public void handleEnter() {
    circle.setOpacity(1);
  }

  @FXML
  public void handleExit() {
    circle.setOpacity(0.8);
  }

  /**
   * Returns whether the hints box is currently visible.
   * 
   * @return true if the hints box is visible, false otherwise
   */
  public void showHintBox() {
    textArea.setVisible(true);
    instructionsLabel.setVisible(true);
  }

  /**
   * Returns whether the hints box is currently visible.
   * 
   * @return true if the hints box is visible, false otherwise
   */
  public void hideHintBox() {
    textArea.setVisible(false);
    instructionsLabel.setVisible(false);
  }

}
