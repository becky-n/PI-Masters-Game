package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.media.AudioClip;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.InstructionsManager;

public class InstructionsController {

  @FXML
  private Rectangle textArea;

  @FXML
  private Label instructionsLabel;

  private AudioClip buttonClickSound;

  private boolean buttonClicked = false;

  public void initialize() {
    // hide the text area
    buttonClickSound = new AudioClip(getClass().getResource("/sounds/click.mp3").toString());
    textArea.setVisible(false);
    instructionsLabel.setVisible(false);
    // Bind the hintsTextArea text property to the hints managed in HintsManager
    InstructionsManager.getInstance().setInstructionsController(this);
    instructionsLabel.setText(InstructionsManager.getInstance().getCurrentInstructions());

  }

  public void updateInstructions(String instructions) {
    // Update the text whenever it changes in the HintsManager
    instructionsLabel.setText(instructions);
    System.out.println("text setted");

  }

  @FXML
  public void hintClicked() {
    buttonClickSound.play();
    // Show the hints text area when the hints button is clicked
    if (buttonClicked) {
      textArea.setVisible(false);
      instructionsLabel.setVisible(false);
      buttonClicked = false;
    } else {
      textArea.setVisible(true);
      instructionsLabel.setVisible(true);
      buttonClicked = true;
    }
  }

}
