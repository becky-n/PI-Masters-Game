package nz.ac.auckland;

import javafx.scene.layout.Pane;

/**
 * Stores the clue menu pane for later use
 */
public class ClueMenu {
  private static Pane storedClueMenuPane;

  /**
   * Gets the stored clue menu pane
   * 
   * @return the stored clue menu pane
   */
  public static Pane getStoredClueMenuPane() {
    return storedClueMenuPane;
  }

  /**
   * Sets the stored clue menu pane
   * 
   * @param pane the pane to store
   */
  public static void setStoredClueMenuPane(Pane pane) {
    storedClueMenuPane = pane;
  }
}
