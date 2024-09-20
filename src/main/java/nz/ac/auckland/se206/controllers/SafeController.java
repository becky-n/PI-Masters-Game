package nz.ac.auckland.se206.controllers;

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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.DraggableMaker;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.InstructionsManager;
import nz.ac.auckland.se206.Navigation;
import nz.ac.auckland.se206.TimerManager;
import java.io.IOException;

public class SafeController {
  public static boolean unlocked = false;
  private AudioClip buttonClickSound;

  @FXML
  private ImageView key;

  @FXML
  private Rectangle target;

  @FXML
  private MenuButton menuButton;
  @FXML
  private Label timerLabel;
  @FXML
  private Pane clueMenu;
  @FXML
  private Label infoLabel;
  @FXML
  private Pane instructionsPane;

  private static GameStateContext context = new GameStateContext();

  /**
   * Initializes the SafeController. Sets up the timer, menu navigation, chat,
   * and loads the clue menu and hints box.
   *
   * @throws IOException if there is an I/O error during initialization
   */
  @FXML
  private void initialize() throws IOException {
    context.setState(context.getGuessingState());
    buttonClickSound = new AudioClip(getClass().getResource("/sounds/click.mp3").toString());

    animateText("There appears to be a key! Would this unlock the jewellery box?");
    // load the clue menu
    try {
      handleClueMenu(clueMenu);
    } catch (IOException e) {
      e.printStackTrace();
    }

    try {
      loadHintsBox(instructionsPane);
    } catch (IOException e) {
      e.printStackTrace();
    }

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

    DraggableMaker dm = new DraggableMaker();
    dm.makeDraggable(key);

    key.setOnMouseReleased(event -> {
      if (isKeyOverTarget(key, target)) {
        unlocked = true;
        try {
          App.setRoot("lock");
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });

  }

  /**
   * Loads the hints box into the provided pane.
   * 
   * @param pane the pane where the hints box will be loaded
   * @throws IOException if there is an I/O error during loading
   */
  private void loadHintsBox(Pane pane) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/instructions.fxml"));
    Pane hintsPane = loader.load();
    pane.getChildren().clear();
    pane.getChildren().add(hintsPane);
  }

  /**
   * Handles the clue menu button click event.
   *
   * @param pane the pane to display the clue menu
   * @throws IOException if there is an I/O error
   */
  private boolean isKeyOverTarget(ImageView key, Rectangle target) {
    return key.getBoundsInParent().intersects(target.getBoundsInParent());
  }

  /**
   * Handles the clue menu button click event.
   *
   * @param pane the pane to display the clue menu
   * @throws IOException if there is an I/O error
   */
  public static boolean isUnlocked() {
    return unlocked;
  }

  /**
   * Handles the clue menu button click event.
   *
   * @param pane the pane to display the clue menu
   * @throws IOException if there is an I/O error
   */
  @FXML
  public static void handleClueMenu(Pane pane) throws IOException {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/clueMenu.fxml"));
    Pane menuPane = loader.load();

    pane.getChildren().clear();
    pane.getChildren().add(menuPane);
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

  /**
   * Handles the guess button click event.
   *
   * @param event the action event triggered by clicking the guess button
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleGuessClick(ActionEvent event) throws IOException {
    buttonClickSound.play();
    boolean[] suspects = ChatController.suspectsTalkedTo();
    boolean[] clues = CrimeController.cluesGuessed();
    boolean allSuspectsTalkedTo = suspects[0] && suspects[1] && suspects[2];
    boolean atLeastOneClueFound = clues[0] || clues[1] || clues[2];
    if (suspects[0] && suspects[1] && suspects[2]) {
      if (clues[0] || clues[1] || clues[2]) {
        context.handleGuessClick();
        App.setRoot("guess");
      }
    } else if (!allSuspectsTalkedTo && atLeastOneClueFound) {
      InstructionsManager.getInstance().updateInstructions(
          "You must talk to all suspects before making a guess.");
      InstructionsManager.getInstance().showInstructions();
    } else if (!atLeastOneClueFound && allSuspectsTalkedTo) {
      InstructionsManager.getInstance().updateInstructions(
          "You must find at least one clue before making a guess.");
      InstructionsManager.getInstance().showInstructions();
    } else {
      InstructionsManager.getInstance().updateInstructions(
          "You must talk to all suspects and find at least one clue before making a guess.");
      InstructionsManager.getInstance().showInstructions();
    }
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

  /**
   * Handles the hover event.
   *
   * @param event the mouse event triggered by hovering over a clue
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onHover(MouseEvent event) throws IOException {

    Rectangle clickedRectangle = (Rectangle) event.getSource();
    context.handleClueClick(event, clickedRectangle.getId());
  }

  /**
   * Handles the hover event.
   *
   * @param event the mouse event triggered by hovering over a clue
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void offHover(MouseEvent event) throws IOException {
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    context.handleClueClick(event, clickedRectangle.getId());
  }
}