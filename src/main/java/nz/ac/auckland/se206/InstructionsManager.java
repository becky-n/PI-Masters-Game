package nz.ac.auckland.se206;

import nz.ac.auckland.se206.controllers.InstructionsController;

/**
 * The {@code InstructionsManager} class is responsible for managing and
 * updating instructions displayed to the user. It follows the Singleton pattern
 * to
 * ensure there is only one instance of this class throughout the application.
 * The
 * class interacts with an {@link InstructionsController} to update and display
 * instruction messages in the UI.
 */
public class InstructionsManager {

  // Static Fields
  private static InstructionsManager instance;

  /**
   * Retrieves the singleton instance of {@code InstructionsManager}.
   * If the instance does not exist, it will be created.
   *
   * @return the singleton instance of {@code InstructionsManager}
   */
  public static InstructionsManager getInstance() {
    if (instance == null) {
      instance = new InstructionsManager();
    }
    return instance;
  }

  // Instance Fields
  private InstructionsController instructionsController;
  private String currentInstructions = "Chat to guests and find clues!";

  // Constructor (private for singleton pattern)
  private InstructionsManager() {
  }

  /**
   * Sets the {@link InstructionsController} instance for this manager.
   * This method will also update the controller with the current instructions.
   *
   * @param controller the {@link InstructionsController} instance to be
   *                   associated with this manager
   */
  public void setInstructionsController(InstructionsController controller) {
    this.instructionsController = controller;
    // Whenever the controller is set, update it with the current instructions
    updateControllerInstructions();
  }

  /**
   * Updates the instructions text managed by this class and notifies the
   * controller
   * to reflect these changes in the UI.
   *
   * @param newInstructions the new instructions text to display
   */
  public void updateInstructions(String newInstructions) {
    currentInstructions = newInstructions;
    updateControllerInstructions();
  }

  /**
   * Sends the current instructions to the {@link InstructionsController} for
   * display.
   * This method checks if a controller is set before attempting to update it.
   */
  private void updateControllerInstructions() {
    if (instructionsController != null) {
      instructionsController.updateInstructions(currentInstructions);
    }
  }

  /**
   * Retrieves the current instructions managed by this class.
   *
   * @return the current instructions text
   */
  public String getCurrentInstructions() {
    return currentInstructions;
  }

  /**
   * Displays the instructions hint box by calling the appropriate method
   * in the {@link InstructionsController}. The hint box provides additional
   * information or guidance to the user.
   */
  public void showInstructions() {
    if (instructionsController != null) {
      instructionsController.showHintBox();
    }
  }

  /**
   * Hides the instructions hint box by calling the appropriate method
   * in the {@link InstructionsController}. This method is used to hide
   * the hint box when it is no longer needed.
   */
  public void hideInstructions() {
    if (instructionsController != null) {
      instructionsController.hintClicked();
    }
  }
}
