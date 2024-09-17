package nz.ac.auckland.se206;

public class InstructionsManager {
  private static InstructionsManager instance;
  private String hintText;

  private InstructionsManager() {
    hintText = "find clues and chat to guests!"; // Default hint
  }

  public static InstructionsManager getInstance() {
    if (instance == null) {
      instance = new InstructionsManager();
    }
    return instance;
  }

  public String getHintText() {
    return hintText;
  }

  public void updateInstructions(String hintText) {
    this.hintText = hintText;
  }
}
