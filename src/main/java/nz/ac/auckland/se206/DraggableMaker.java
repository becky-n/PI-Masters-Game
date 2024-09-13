package nz.ac.auckland.se206;

import javafx.scene.Node;
import javafx.scene.media.AudioClip;

public class DraggableMaker {

  private double mouseAnchorX;
  private double mouseAnchorY;
  private AudioClip glassSound;

  public void makeDraggable(Node node) {
    glassSound = new AudioClip(getClass().getResource("/sounds/glass.mp3").toString());

    node.setOnMousePressed(mouseEvent -> {
      glassSound.play();
      mouseAnchorX = mouseEvent.getX();
      mouseAnchorY = mouseEvent.getY();
    });

    node.setOnMouseDragged(mouseEvent -> {
      node.setLayoutX(mouseEvent.getSceneX() - mouseAnchorX);
      node.setLayoutY(mouseEvent.getSceneY() - mouseAnchorY);
    });
  }

}
