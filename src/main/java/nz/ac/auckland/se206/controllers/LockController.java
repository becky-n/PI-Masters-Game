package nz.ac.auckland.se206.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.transform.Rotate;
import javafx.scene.image.ImageView;

public class LockController {
  private double angle = 0; // Keeps track of the current rotation angle
  
  @FXML
  private ImageView key;

  @FXML
  public void initialize() {
    // Initialization logic if necessary
  }

  @FXML
  private void handleLeftRotate() {
    angle -= 90; // Rotate left by 90 degrees
    rotateImage(15, 75, Rotate.Z_AXIS); // Rotate around the point (100, 100) on the Z-axis
  }

  @FXML
  private void handleRightRotate() {
    angle += 90; // Rotate right by 90 degrees
    rotateImage(15, 75, Rotate.Z_AXIS); // Rotate around the point (100, 100) on the Z-axis
  }

  private void rotateImage(double pivotX, double pivotY, javafx.geometry.Point3D axis) {
    Rotate rotate = new Rotate(angle, pivotX, pivotY); // Set pivot point for rotation
    rotate.setAxis(axis); // Set rotation axis (Z_AXIS, X_AXIS, or Y_AXIS)
    key.getTransforms().clear(); // Clear previous transforms
    key.getTransforms().add(rotate); // Apply the new rotation
  }
}
