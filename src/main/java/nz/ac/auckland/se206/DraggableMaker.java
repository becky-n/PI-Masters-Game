package nz.ac.auckland.se206;

import javafx.scene.Node;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Makes a node draggable.
 */
public class DraggableMaker {

  private double mouseAnchorX;
  private double mouseAnchorY;
  private MediaPlayer glassSound;

  /**
   * Makes a node draggable by the user.
   *
   * @param node the node to make draggable
   */
  public void makeDraggable(Node node) {
    // Any required initialization code can be placed here
    Media glassMedia = new Media(getClass().getResource("/sounds/glass.mp3").toString());
    glassSound = new MediaPlayer(glassMedia);

    // add sound to array
    App.addSound(glassSound);
    App.muteSound();

    // When the node is pressed, record the position of the mouse click
    node.setOnMousePressed(mouseEvent -> {
      glassSound.seek(javafx.util.Duration.ZERO);
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
