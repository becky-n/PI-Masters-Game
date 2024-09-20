package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.DraggableMaker;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.Navigation;
import nz.ac.auckland.se206.TimerManager;

public class WindowController {
  public static boolean fabricFound = false;
  private static GameStateContext context = new GameStateContext();

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

  private AudioClip buttonClickSound;
  private AudioClip twinkleSound;

  /**
   * Initializes the WindowController. Sets up the timer, menu navigation, chat,
   * and loads the clue menu and hints box.
   *
   * @throws IOException if there is an I/O error during initialization
   */
  @FXML
  private void initialize() throws IOException {

    // if they have already found the fabric
    if (fabricFound) {
      fabric.setImage(new Image("/images/fabric4.png"));
      animateText("A torn piece of black fabric… it seems familiar, "
          + "but where have I seen it before?");

      // hide the glass images
      glass1.setVisible(false);
      glass2.setVisible(false);
      glass3.setVisible(false);
      glass4.setVisible(false);
      glass5.setVisible(false);
    } else {
      animateText(
          "It seems there's something hidden beneath the broken glass… "
              + "try moving the shards aside to"
              + " uncover it.");

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
    try {
      handleClueMenu(clueMenu);
    } catch (IOException e) {
      e.printStackTrace();
    }
    buttonClickSound = new AudioClip(getClass().getResource("/sounds/click.mp3").toString());
    twinkleSound = new AudioClip(getClass().getResource("/sounds/twinkle.mp3").toString());

    // Initialize the controller
    Navigation nav = new Navigation();
    nav.setMenu(menuButton);

    TimerManager timerManager = TimerManager.getInstance();

    // Bind the timerLabel to the timeRemaining property
    timerLabel
        .textProperty()
        .bind(
            Bindings.createStringBinding(
                () -> String.format(
                    "%02d:%02d",
                    timerManager.getTimeRemaining() / 60, timerManager.getTimeRemaining() % 60),
                timerManager.timeRemainingProperty()));
  }

  /**
   * Handles the guess button click event.
   *
   * @param event the action event triggered by clicking the guess button
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleGuessClick(ActionEvent event) throws IOException {
    // play the button click sound
    buttonClickSound.play();

    // check if the player has talked to all suspects and guessed all clues
    boolean[] suspects = ChatController.suspectsTalkedTo();
    boolean[] clues = CrimeController.cluesGuessed();

    // if they have talked to all suspects and guessed all clues, go to the guess
    // screen
    if (suspects[0] && suspects[1] && suspects[2]) {
      if (clues[0] || clues[1] || clues[2]) {
        context.handleGuessClick();
        App.setRoot("guess");
      }

    }
  }

  /**
   * Loads the clue menu into the specified pane.
   *
   * @param pane the pane to which the clue menu should be added
   * @throws IOException if there is an I/O error during loading the clue menu
   */
  @FXML
  public static void handleClueMenu(Pane pane) throws IOException {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/clueMenu.fxml"));
    Pane menuPane = loader.load();

    pane.getChildren().clear();
    pane.getChildren().add(menuPane);
  }

  /**
   * Handles the glass image click event.
   */
  @FXML
  public void onFabricClick() {
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
      animateText("A torn piece of black fabric… "
          + "it seems familiar, but where have I seen it before?");
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
  public void onBack() throws IOException {
    buttonClickSound.play();
    App.setRoot("crime");
  }

  /** Animates the text in the info label. */
  private void animateText(String str) {
    final IntegerProperty i = new SimpleIntegerProperty(0);
    Timeline timeline = new Timeline();
    KeyFrame keyFrame = new KeyFrame(
        Duration.seconds(0.015), // Adjusted for smoother animation
        event -> {
          if (i.get() > str.length()) {
            timeline.stop();
          } else {
            infoLabel.setText(str.substring(0, i.get()));
            i.set(i.get() + 1);
          }
        });
    timeline.getKeyFrames().add(keyFrame);
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
  }
}
