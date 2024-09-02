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

    MenuItem gerald = new MenuItem("Gerald");
    MenuItem jesin = new MenuItem("Jesin");
    MenuItem crimeScene = new MenuItem("Crime Scene");
    MenuItem andrea = new MenuItem("Andrea");

    menuButton.getItems().addAll(gerald, jesin, crimeScene, andrea);


    // Set the action
    gerald.setOnAction(e -> { changeScene("aisle"); buttonClickSound.play(); });
    jesin.setOnAction(e -> {changeScene("ballroom"); buttonClickSound.play(); });
    crimeScene.setOnAction(e -> {changeScene("crime"); buttonClickSound.play(); });
    andrea.setOnAction(e -> {changeScene("lobby");buttonClickSound.play(); });

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
