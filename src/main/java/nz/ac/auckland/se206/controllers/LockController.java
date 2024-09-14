package nz.ac.auckland.se206.controllers;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.transform.Rotate;
import nz.ac.auckland.se206.App;
import javafx.scene.image.ImageView;
import java.io.IOException;

public class LockController {
  private double angle = 0; 
  private List<String> expectedSequence = List.of("right", "left", "right", "right"); 
  private List<String> userSequence = new ArrayList<>();
  
  @FXML
  private ImageView key;

  @FXML
  public void initialize() {
    // Initialization logic if necessary
  }

  @FXML
  private void handleLeftRotate() {
    angle -= 90; // Rotate left by 90 degrees
    rotateImage(15, 75, Rotate.Z_AXIS); 
    trackAction("left");
  }

  @FXML
  private void handleRightRotate() {
    angle += 90; // Rotate right by 90 degrees
    rotateImage(15, 75, Rotate.Z_AXIS); 
    trackAction("right");
  }

  private void rotateImage(double pivotX, double pivotY, javafx.geometry.Point3D axis) {
    Rotate rotate = new Rotate(angle, pivotX, pivotY); 
    rotate.setAxis(axis); 
    key.getTransforms().clear(); 
    key.getTransforms().add(rotate); 
  }

  private void trackAction(String action) {
    userSequence.add(action); 

    // Check if the user sequence matches the expected sequence
    if (userSequence.equals(expectedSequence)) {
       try {
           App.setRoot("unlockBox");
       } catch (IOException e) {
           e.printStackTrace();
       }
    
        userSequence.clear(); 
    } 
}
}
