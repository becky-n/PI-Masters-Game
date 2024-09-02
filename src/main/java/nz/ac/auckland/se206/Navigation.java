package nz.ac.auckland.se206;

import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import java.io.IOException; 


public class Navigation {

  public Navigation() {
  }

  public void setMenu(MenuButton menuButton) {
    // Set the menu
    menuButton.getItems().clear();

    MenuItem gerald = new MenuItem("Gerald");
    MenuItem jesin = new MenuItem("Jesin");
    MenuItem crimeScene = new MenuItem("Crime Scene");
    MenuItem andrea = new MenuItem("Andrea");

    menuButton.getItems().addAll(gerald, jesin, crimeScene, andrea);

    // Set the action
    gerald.setOnAction(e -> changeScene("aisle"));
    jesin.setOnAction(e -> changeScene("ballroom"));
    crimeScene.setOnAction(e -> changeScene("crime"));
    andrea.setOnAction(e -> changeScene("lobby"));

  }

  private void changeScene(String scene) {
    try {
      App.setRoot(scene);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

}
