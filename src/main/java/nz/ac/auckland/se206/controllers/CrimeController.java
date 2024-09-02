package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import nz.ac.auckland.se206.Navigation;
import javafx.scene.control.MenuButton;

public class CrimeController {
  @FXML
  private MenuButton menuButton;

  @FXML
  private void initialize() {
    // Initialize the controller
  Navigation nav = new Navigation();
  nav.setMenu(menuButton);

  
  }
  
}
