package nz.ac.auckland.se206;

import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.media.AudioClip;

import java.io.IOException;

public class Navigation {
  private AudioClip buttonClickSound;

  public Navigation() {
  }

  public void setMenu(MenuButton menuButton) {
    buttonClickSound = new AudioClip(getClass().getResource("/sounds/click.mp3").toString());
    // Set the menu
    menuButton.getItems().clear();

    MenuItem crimeScene = new MenuItem("Crime Scene");
    MenuItem gerald = new MenuItem("Gerald (The Plus One)");
    MenuItem jesin = new MenuItem("Jesin (The Mother In Law)");
    MenuItem andrea = new MenuItem("Andrea (The Bridesmaid)");

    String style = "-fx-text-fill: white; -fx-padding: 0 10px 0 10px;";
    gerald.setStyle(style);
    jesin.setStyle(style);
    crimeScene.setStyle(style);
    andrea.setStyle(style);

    menuButton.getItems().addAll(crimeScene, jesin, gerald, andrea);

    // Set the action
    gerald.setOnAction(e -> {
      changeScene("aisle");
      buttonClickSound.play();
    });
    jesin.setOnAction(e -> {
      changeScene("lobby");
      buttonClickSound.play();
    });
    crimeScene.setOnAction(e -> {
      changeScene("crime");
      buttonClickSound.play();
    });
    andrea.setOnAction(e -> {
      changeScene("ballroom");
      buttonClickSound.play();
    });

    menuButton.setOnShowing(e -> buttonClickSound.play());
  }

  private void changeScene(String scene) {
    try {
      App.setRoot(scene);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

}
