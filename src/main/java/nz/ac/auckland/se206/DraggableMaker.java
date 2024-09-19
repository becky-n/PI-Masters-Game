package nz.ac.auckland.se206;

import javafx.scene.Node;
import javafx.scene.media.AudioClip;

/**
 * Makes a node draggable.
 */
public class DraggableMaker {

  private double mouseAnchorX;
  private double mouseAnchorY;
  private AudioClip glassSound;

  /**
   * Makes a node draggable.
   *
   * @param node the node to make draggable
   */
  public void makeDraggable(Node node) {
    glassSound = new AudioClip(getClass().getResource("/sounds/glass.mp3").toString());
    // When the node is pressed, record the position of the mouse click
    node.setOnMousePressed(mouseEvent -> {
      glassSound.play();
      mouseAnchorX = mouseEvent.getX();
      mouseAnchorY = mouseEvent.getY();
    });
    // When the node is dragged, move it to the new position
    node.setOnMouseDragged(mouseEvent -> {
      node.setLayoutX(mouseEvent.getSceneX() - mouseAnchorX);
      node.setLayoutY(mouseEvent.getSceneY() - mouseAnchorY);
    });
  }

}
