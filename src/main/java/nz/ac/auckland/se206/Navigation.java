package nz.ac.auckland.se206;

import java.io.IOException;

import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.media.AudioClip;

public class Navigation {
  private AudioClip buttonClickSound;
  private AudioClip doorSound;

  public Navigation() {
  }

  public void setMenu(MenuButton menuButton) {
    buttonClickSound = new AudioClip(getClass().getResource("/sounds/click.mp3").toString());
    doorSound = new AudioClip(getClass().getResource("/sounds/door.mp3").toString());
    // Set the menu
    menuButton.getItems().clear();

    MenuItem crimeScene = new MenuItem("Crime Scene");
    MenuItem gerald = new MenuItem("Gerald | Plus One");
    MenuItem jesin = new MenuItem("Jesin | Mother In Law ");
    MenuItem andrea = new MenuItem("Andrea | Bridesmaid");

    String style = "-fx-text-fill: white; -fx-padding: 0 10px 0 10px;";
    gerald.setStyle(style);
    jesin.setStyle(style);
    crimeScene.setStyle(style);
    andrea.setStyle(style);

    String currentSceneId = App.getCurrentSceneId(); // Get the ID of the current scene

    // Add menu items based on the current scene using switch statement
    switch (currentSceneId) {
      case "crime":
        menuButton.getItems().addAll(jesin, gerald, andrea);
        break;
      case "lobby":
        menuButton.getItems().addAll(crimeScene, gerald, andrea);
        break;
      case "aisle":
        menuButton.getItems().addAll(crimeScene, jesin, andrea);
        break;
      case "ballroom":
        menuButton.getItems().addAll(crimeScene, jesin, gerald);
        break;
      default:
        menuButton.getItems().addAll(crimeScene, jesin, gerald, andrea);
    }

    // Set the action
    gerald.setOnAction(e -> {
      changeScene("aisle");
      doorSound.play();
    });
    jesin.setOnAction(e -> {
      changeScene("lobby");
      doorSound.play();
    });
    crimeScene.setOnAction(e -> {
      changeScene("crime");
      doorSound.play();
    });
    andrea.setOnAction(e -> {
      changeScene("ballroom");
      doorSound.play();
    });

    menuButton.setOnShowing(e -> buttonClickSound.play());
  }

  private void changeScene(String scene) {
    try {
      App.setRoot(scene);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
