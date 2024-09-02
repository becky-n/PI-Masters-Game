package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import nz.ac.auckland.se206.Navigation;

public class SuiteController {
  @FXML
  private MenuButton menuButton;

  @FXML
  private void initialize() {
    // Initialize the controller
  Navigation nav = new Navigation();
  nav.setMenu(menuButton);

  
  }
}
