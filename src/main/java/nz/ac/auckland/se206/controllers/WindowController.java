package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.DraggableMaker;
import nz.ac.auckland.se206.Navigation;

public class WindowController {
  public static boolean fabricFound = false;

  /**
   * Resets the fabricFound variable to false.
   */
  public static void resetFabric() {
    fabricFound = false;
  }

  /**
   * Returns whether the fabric has been found.
   *
   * @return true if the fabric has been found, false otherwise
   */
  public static boolean fabricFound() {
    if (fabricFound) {
      return true;
    } else {
      return false;
    }
  }

  @FXML
  private MenuButton menuButton;
  @FXML
  private Label timerLabel;
  @FXML
  private Pane clueMenu;
  @FXML
  private ImageView glass1;
  @FXML
  private ImageView glass2;
  @FXML
  private ImageView glass3;
  @FXML
  private ImageView glass4;
  @FXML
  private ImageView glass5;
  @FXML
  private Label infoLabel;
  @FXML
  private ImageView fabric;
  @FXML
  private Pane instructionsPane;
  @FXML
  private Circle redCircle;
  @FXML
  private ImageView clock;
  @FXML
  private Pane mutePane;

  private MediaPlayer buttonClickSound;
  private MediaPlayer twinkleSound;

  /**
   * Initializes the WindowController. Sets up the timer, menu navigation, chat,
   * and loads the clue menu and hints box.
   *
   * @throws IOException if there is an I/O error during initialization
   */
  @FXML
  private void initialize() throws IOException {

    // set circle colour for time almost out
    App.setRedCircle(redCircle, clock);

    // if they have already found the fabric
    if (fabricFound) {
      fabric.setImage(new Image("/images/fabric4.png"));
      App.animateText("A torn piece of black fabric… it seems familiar, "
          + "but where have I seen it before?", infoLabel);

      // hide the glass images
      glass1.setVisible(false);
      glass2.setVisible(false);
      glass3.setVisible(false);
      glass4.setVisible(false);
      glass5.setVisible(false);
    } else {
      App.animateText(
          "It seems there's something hidden beneath the broken glass… "
              + "try moving the shards aside to"
              + " uncover it.",
          infoLabel);

      // show the glass images (in the case of they are replaying the game)
      glass1.setVisible(true);
      glass2.setVisible(true);
      glass3.setVisible(true);
      glass4.setVisible(true);
      glass5.setVisible(true);
    }

    // Make the glass images draggable
    DraggableMaker dm = new DraggableMaker();
    dm.makeDraggable(glass1);
    dm.makeDraggable(glass2);
    dm.makeDraggable(glass3);
    dm.makeDraggable(glass4);
    dm.makeDraggable(glass5);

    // load the clue menu
    App.handleClueMenu(clueMenu);

    App.loadHintsBox(instructionsPane);

    Media buttonClickMedia = new Media(getClass().getResource("/sounds/click.mp3").toString());
    Media twinkleMedia = new Media(getClass().getResource("/sounds/twinkle.mp3").toString());

    buttonClickSound = new MediaPlayer(buttonClickMedia);
    twinkleSound = new MediaPlayer(twinkleMedia);

    // create array of sounds and store
    App.handleMute(mutePane);
    ArrayList<MediaPlayer> sounds = new ArrayList<MediaPlayer>();
    sounds.add(buttonClickSound);
    sounds.add(twinkleSound);

    App.setSounds(sounds);
    App.muteSound();

    // Initialize the controller
    Navigation nav = new Navigation();
    nav.setMenu(menuButton);

    App.timer(timerLabel);
  }

  /**
   * Handles the guess button click event.
   *
   * @param event the action event triggered by clicking the guess button
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onHandleGuessClick(ActionEvent event) throws IOException {
    buttonClickSound.seek(javafx.util.Duration.ZERO);

    // play the button click sound
    buttonClickSound.play();
    App.guessClick();
  }

  /**
   * Handles the glass image click event.
   */
  @FXML
  public void onFabricClick() {
    buttonClickSound.seek(javafx.util.Duration.ZERO);
    buttonClickSound.play();
    // if image is fabric1, change to fabric2
    if (fabric.getImage().getUrl().contains("fabric1")) {
      fabric.setImage(new Image("/images/fabric2.png"));
    } else if (fabric.getImage().getUrl().contains("fabric2")) {
      fabric.setImage(new Image("/images/fabric3.png"));
    } else if (fabric.getImage().getUrl().contains("fabric3")) {
      fabric.setImage(new Image("/images/fabric4.png"));
      twinkleSound.play();
      fabricFound = true;
      App.animateText("A torn piece of black fabric… "
          + "it seems familiar, but where have I seen it before?", infoLabel);
    }

    onHoverFabric();
  }

  /**
   * Handles the hover event for the fabric image.
   */
  @FXML
  public void onHoverFabric() {
    if (fabric.getImage().getUrl().contains("fabric1")) {
      // change to fabric1hover
      fabric.setImage(new Image("/images/fabric1hover.png"));
    } else if (fabric.getImage().getUrl().contains("fabric2")) {
      // change to fabric2hover
      fabric.setImage(new Image("/images/fabric2hover.png"));
    } else if (fabric.getImage().getUrl().contains("fabric3")) {
      // change to fabric3hover
      fabric.setImage(new Image("/images/fabric3hover.png"));
    }
  }

  /**
   * Handles the off hover event for the fabric image.
   */
  @FXML
  public void offHoverFabric() {
    if (fabric.getImage().getUrl().contains("fabric1")) {
      // change to fabric1
      fabric.setImage(new Image("/images/fabric1.png"));
    } else if (fabric.getImage().getUrl().contains("fabric2")) {
      // change to fabric2
      fabric.setImage(new Image("/images/fabric2.png"));
    } else if (fabric.getImage().getUrl().contains("fabric3")) {
      // change to fabric3
      fabric.setImage(new Image("/images/fabric3.png"));
    }
  }

  /**
   * Handles the back button click event.
   *
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onBack() throws IOException {
    buttonClickSound.seek(javafx.util.Duration.ZERO);

    buttonClickSound.play();
    App.setRoot("crime");
  }

}
