package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.DraggableMaker;
import java.io.IOException;

public class SafeController {

  @FXML
  private ImageView key;

  @FXML
  private Rectangle target;

  @FXML
  private void initialize() throws IOException {
    DraggableMaker dm = new DraggableMaker();
    dm.makeDraggable(key);

    key.setOnMouseReleased(event -> {
      if (isKeyOverTarget(key, target)) {
        try {
          App.setRoot("lock");
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });

  }

  private boolean isKeyOverTarget(ImageView key, Rectangle target) {
    return key.getBoundsInParent().intersects(target.getBoundsInParent());
  }
}