package nz.ac.auckland.se206;

import nz.ac.auckland.se206.controllers.InstructionsController;

public class InstructionsManager {
  private static InstructionsManager instance;
  private String currentInstructions = "Chat to guests and find clues!";
  private InstructionsController instructionsController;

  // Singleton pattern to ensure only one instance
  private InstructionsManager() {
  }

  public static InstructionsManager getInstance() {
    if (instance == null) {
      instance = new InstructionsManager();
    }
    return instance;
  }

  // Sets the InstructionsController instance
  public void setInstructionsController(InstructionsController controller) {
    this.instructionsController = controller;
    // Whenever the controller is set, update it with the current instructions
    updateControllerInstructions();
  }

  // Updates the instructions text and notifies the controller
  public void updateInstructions(String newInstructions) {
    currentInstructions = newInstructions;
    updateControllerInstructions();
  }

  // Sets the instructions text in the controller
  private void updateControllerInstructions() {
    if (instructionsController != null) {
      instructionsController.updateInstructions(currentInstructions);
    }
  }

  // Gets the current instructions text
  public String getCurrentInstructions() {
    return currentInstructions;
  }
}
