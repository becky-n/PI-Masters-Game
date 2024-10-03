package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.DraggableMaker;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.Navigation;

/**
 * The SafeController class is responsible for managing the interactions and
 * events within the safe scene of the application. It handles the
 * initialization
 * of the scene, including setting up the timer, menu navigation, chat, and
 * loading
 * the clue menu and hints box. It also manages the draggable key and its
 * interaction
 * with the target, as well as handling various button click events.
 */
public class SafeController {
  public static boolean unlocked = false;
  private static GameStateContext context = new GameStateContext();

  /**
   * Handles the clue menu button click event.
   *
   * @param pane the pane to display the clue menu
   * @throws IOException if there is an I/O error
   */
  public static boolean isUnlocked() {
    return unlocked;
  }

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
  @FXML
  private Circle redCircle;
  @FXML
  private ImageView clock;

  private AudioClip buttonClickSound;

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

    App.animateText("There appears to be a key! Would this unlock the jewellery box?", infoLabel);
    // load the clue menu
    App.handleClueMenu(clueMenu);

    // set circle colour for time almost out
    App.setRedCircle(redCircle, clock);

    App.loadHintsBox(instructionsPane);

    // Initialize the controller
    Navigation nav = new Navigation();
    nav.setMenu(menuButton);

    App.timer(timerLabel);

    // Make the key draggable
    DraggableMaker dm = new DraggableMaker();
    dm.makeDraggable(key);

    key.setOnMouseReleased(event -> {
      // Check if the key is over the target, set scene
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
   * Handles the clue menu button click event.
   *
   * @param pane the pane to display the clue menu
   * @throws IOException if there is an I/O error
   */
  private boolean isKeyOverTarget(ImageView key, Rectangle target) {
    // Check if the key is over the target
    return key.getBoundsInParent().intersects(target.getBoundsInParent());
  }

  /**
   * Handles the back button click event.
   *
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onBack() throws IOException {
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
  private void onHandleGuessClick(ActionEvent event) throws IOException {
    buttonClickSound.play();
    App.guessClick();
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