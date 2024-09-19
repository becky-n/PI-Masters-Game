package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.media.AudioClip;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.InstructionsManager;

public class InstructionsController {

  @FXML
  private Rectangle textArea;

  @FXML
  private Label instructionsLabel;

  private AudioClip buttonClickSound;

  /**
   * Initializes the controller.
   */
  public void initialize() {
    // hide the text area
    buttonClickSound = new AudioClip(getClass().getResource("/sounds/click.mp3").toString());
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
    System.out.println("text setted");

  }

  /**
   * Handles the hints button being clicked.
   */
  @FXML
  public void hintClicked() {
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
