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
  private AudioClip buttonClickSound;
  private AudioClip twinkleSound;

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

  private static GameStateContext context = new GameStateContext();

  @FXML
  private void initialize() throws IOException {

    // initialize fabric image
    fabric.setImage(new Image("/images/fabric1.png"));

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

    // animate the text in the info label
    animateText(
        "It seems there's something hidden beneath the broken glass… try moving the shards aside to"
            + " uncover it.");
  }

  /**
   * Handles the guess button click event.
   *
   * @param event the action event triggered by clicking the guess button
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleGuessClick(ActionEvent event) throws IOException {
    buttonClickSound.play();
    App.setRoot("guess");
    context.handleGuessClick();
  }

  @FXML
  public static void handleClueMenu(Pane pane) throws IOException {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/clueMenu.fxml"));
    Pane menuPane = loader.load();

    pane.getChildren().clear();
    pane.getChildren().add(menuPane);
  }

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
      animateText("A torn piece of black fabric… it seems familiar, but where have I seen it before?");
    }

    onHoverFabric();
  }

  @FXML
  public void onHoverFabric() {
    if (fabric.getImage().getUrl().contains("fabric1")) {
      fabric.setImage(new Image("/images/fabric1hover.png"));
    } else if (fabric.getImage().getUrl().contains("fabric2")) {
      fabric.setImage(new Image("/images/fabric2hover.png"));
    } else if (fabric.getImage().getUrl().contains("fabric3")) {
      fabric.setImage(new Image("/images/fabric3hover.png"));
    }
  }

  @FXML
  public void offHoverFabric() {
    if (fabric.getImage().getUrl().contains("fabric1")) {
      fabric.setImage(new Image("/images/fabric1.png"));
    } else if (fabric.getImage().getUrl().contains("fabric2")) {
      fabric.setImage(new Image("/images/fabric2.png"));
    } else if (fabric.getImage().getUrl().contains("fabric3")) {
      fabric.setImage(new Image("/images/fabric3.png"));
    }
  }

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
