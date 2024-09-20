package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.media.AudioClip;
import javafx.scene.shape.Circle;

/*
 * Navigation class to handle the navigation between scenes
 */
public class Navigation {
  private AudioClip buttonClickSound;
  private AudioClip doorSound;

  /*
   * Set the menu for the navigation
   */
  public void setMenu(MenuButton menuButton) {
    buttonClickSound = new AudioClip(getClass().getResource("/sounds/click.mp3").toString());
    doorSound = new AudioClip(getClass().getResource("/sounds/door.mp3").toString());
    // Set the menu
    menuButton.getItems().clear();

    // Create custom MenuItems
    MenuItem crimeScene = createCustomMenuItem("Bridal Suite", "/images/bridalSuite.png");
    MenuItem gerald = createCustomMenuItem("The Aisle", "/images/madGerald.png");
    MenuItem jesin = createCustomMenuItem("The Lobby", "/images/madJesin.png");
    MenuItem andrea = createCustomMenuItem("The Ballroom", "/images/madAndrea.png");

    String style = "-fx-text-fill: white; -fx-padding: 0 10px 0 10px;";
    gerald.setStyle(style);
    jesin.setStyle(style);
    crimeScene.setStyle(style);
    andrea.setStyle(style);

    String currentSceneId = App.getCurrentSceneId(); // Get the ID of the current scene

    // Add menu items based on the current scene using switch statement
    switch (currentSceneId) {
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
        menuButton.getItems().addAll(jesin, gerald, andrea);
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

  /*
   * Create a custom MenuItem with a circular image and label
   */
  private MenuItem createCustomMenuItem(String text, String imagePath) {
    ImageView imageView = new ImageView(new Image(getClass().getResource(imagePath).toString()));

    // Set the size of the image
    imageView.setFitWidth(50);
    imageView.setFitHeight(50);
    imageView.setPreserveRatio(true);

    // Create a Circle to clip the ImageView
    Circle clip = new Circle(25, 25, 25); // Center at (25, 25) for a 50x50 image
    imageView.setClip(clip);

    // Create a Label and set the style
    Label label = new Label(text);
    label.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

    // Create HBox and set alignment
    HBox hbox = new HBox(10, imageView, label);
    hbox.setStyle("-fx-padding: 5;");
    hbox.setAlignment(Pos.CENTER_LEFT); // Center vertically within the HBox

    // Create a MenuItem and set the graphic
    MenuItem menuItem = new MenuItem();
    menuItem.setGraphic(hbox);

    return menuItem;
  }

  /*
   * Change the scene
   */
  private void changeScene(String scene) {
    try {
      App.setRoot(scene);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
