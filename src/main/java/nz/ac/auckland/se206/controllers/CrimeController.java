package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.beans.binding.Bindings;
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
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.InstructionsManager;
import nz.ac.auckland.se206.Navigation;
import nz.ac.auckland.se206.TimerManager;


/**
 * Controller class for the Crime scene.
 * Manages the initialization of the scene, handling button clicks, and updating
 * UI elements.
 */
public class CrimeController {
  private static GameStateContext context = new GameStateContext();
  // set clues to false by default
  @FXML
  private static boolean safe = false;
  @FXML
  private static boolean glass = false;
  @FXML
  private static boolean letter = false;

  /**
   * Resets the clues by setting the safe, glass, and letter variables to false.
   * This method is typically used to clear any previously set clues and start
   * fresh.
   */
  public static void resetClues() {
    // reset all of the clues to false (not found)
    safe = false;
    glass = false;
    letter = false;
  }

  /**
   * This method returns an array of boolean values representing whether
   * specific clues have been guessed. The array contains three elements:
   * <ul>
   * <li>clues[0] - represents whether clue1 (safe) has been guessed</li>
   * <li>clues[1] - represents whether clue2 (glass) has been guessed</li>
   * <li>clues[2] - represents whether clue3 (letter) has been guessed</li>
   * </ul>
   *
   * @return a boolean array where each element indicates if a corresponding clue
   *         has been guessed
   */
  public static boolean[] cluesGuessed() {
    boolean[] clues = new boolean[3];

    clues[0] = safe; // represents clue1
    clues[1] = glass; // represents clue2
    clues[2] = letter; // represents clue3
    return clues;
  }

  @FXML
  private MenuButton menuButton;
  @FXML
  private Label timerLabel;
  @FXML
  private Pane clueMenu;
  @FXML
  private ImageView safeGlow;
  @FXML
  private ImageView glassPileGlow;
  @FXML
  private ImageView invitationGlow;
  @FXML
  private Pane instructionsPane;

  // Sound effects
  private AudioClip buttonClickSound;
  private AudioClip twinkleSound;
  private AudioClip paperSound;
  private AudioClip glassSound;
  private AudioClip boxSound;

  /**
   * Initializes the CrimeController. Sets up the timer, menu navigation, chat,
   * and loads the clue menu and hints box.
   * 
   * @throws IOException if there is an I/O error during initialization
   */
  @FXML
  private void initialize() throws IOException {
    // call clue menu
    handleClueMenu(clueMenu);

    // load sound effects
    buttonClickSound = new AudioClip(getClass().getResource("/sounds/click.mp3").toString());
    twinkleSound = new AudioClip(getClass().getResource("/sounds/twinkle.mp3").toString());
    paperSound = new AudioClip(getClass().getResource("/sounds/paper.mp3").toString());
    glassSound = new AudioClip(getClass().getResource("/sounds/glass.mp3").toString());
    boxSound = new AudioClip(getClass().getResource("/sounds/box.mp3").toString());

    // set hover effects invisible
    safeGlow.setVisible(false);
    glassPileGlow.setVisible(false);
    invitationGlow.setVisible(false);

    // load interface elements
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
    timerLabel.textProperty().bind(
        Bindings.createStringBinding(() -> String.format("%02d:%02d",
            timerManager.getTimeRemaining() / 60,
            timerManager.getTimeRemaining() % 60),
            timerManager.timeRemainingProperty()));
  }

  /**
   * Handles the hover event for the clues.
   *
   * @param event the mouse event triggered by hovering over a clue
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onHover(MouseEvent event) throws IOException {
    // handle hover for clues
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    context.handleClueClick(event, clickedRectangle.getId());
    if (clickedRectangle.getId().equals("safe")) {
      boxSound.play();
      safeGlow.setVisible(true);
    } else if (clickedRectangle.getId().equals("glass")) {
      glassSound.play();
      glassPileGlow.setVisible(true);
    } else if (clickedRectangle.getId().equals("letter")) {
      paperSound.play();
      invitationGlow.setVisible(true);
    }
  }

  /**
   * Handles the off hover event for the clues.
   *
   * @param event the mouse event triggered by moving the mouse off a clue
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void offHover(MouseEvent event) throws IOException {
    // handle mouse off hover for clues
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    context.handleClueClick(event, clickedRectangle.getId());
    if (clickedRectangle.getId().equals("safe")) {
      boxSound.stop();
      safeGlow.setVisible(false);
    } else if (clickedRectangle.getId().equals("glass")) {
      glassSound.stop();
      glassPileGlow.setVisible(false);
    } else if (clickedRectangle.getId().equals("letter")) {
      paperSound.stop();
      invitationGlow.setVisible(false);
    }
  }

  /**
   * Handles the click event for the clues.
   *
   * @param event the mouse event triggered by clicking a clue
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleClueClick(MouseEvent event) throws IOException {
    buttonClickSound.play();
    twinkleSound.play();

    // get clicked rectangle and handle click
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    context.handleClueClick(event, clickedRectangle.getId());

    // set clue scene based on clicked rectangle
    if (clickedRectangle.getId().equals("safe")) {
      // if clue is found set to different scene
      if (LockController.isBoxUnlocked()) {
        App.setRoot("unlockBox");
        return;
      }
      App.setRoot("safe");
      safe = true;
      return;

    }
    if (clickedRectangle.getId().equals("glass")) {
      App.setRoot("window");
      glass = true;
      return;
    }
    if (clickedRectangle.getId().equals("letter")) {
      boolean isBurnt = LetterCloseUpController.burnt;
      // if clue is found set to different scene
      if (isBurnt) {
        App.setRoot("letterCloseUp");
        return;
      }
      App.setRoot("letter");
      letter = true;
      return;
    }

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
    // initialize suspects and clues to be stored in arrays
    boolean[] suspects = ChatController.suspectsTalkedTo();
    boolean[] clues = CrimeController.cluesGuessed();

    // check if all suspects have been talked to and at least one clue has been
    // found
    boolean allSuspectsTalkedTo = suspects[0] && suspects[1] && suspects[2];
    boolean atLeastOneClueFound = clues[0] || clues[1] || clues[2];

    if (suspects[0] && suspects[1] && suspects[2]) {
      if (clues[0] || clues[1] || clues[2]) {
        context.handleGuessClick();
        App.setRoot("guess");
      }
    } else if (!allSuspectsTalkedTo && atLeastOneClueFound) {
      InstructionsManager.getInstance().updateInstructions(
          "You must talk to all suspects before making a guess."
      );
      InstructionsManager.getInstance().showInstructions();
  } else if (!atLeastOneClueFound && allSuspectsTalkedTo) {
      InstructionsManager.getInstance().updateInstructions(
          "You must find at least one clue before making a guess."
      );
      InstructionsManager.getInstance().showInstructions();
  } else {
      InstructionsManager.getInstance().updateInstructions(
          "You must talk to all suspects and find at least one clue before making a guess."
      );
      InstructionsManager.getInstance().showInstructions();
  }  
  }

  /**
   * Loads the clue menu into the specified pane.
   * 
   * @param pane the pane to which the clue menu should be added
   * @throws IOException if there is an I/O error during loading the clue menu
   */
  @FXML
  public void handleClueMenu(Pane pane) throws IOException {
    // load clue menu
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/clueMenu.fxml"));
    Pane menuPane = loader.load();

    pane.getChildren().clear();
    pane.getChildren().add(menuPane);

  }

  /**
   * Loads the hints box into the specified pane.
   * 
   * @param pane the pane to which the hints box should be added
   * @throws IOException if there is an I/O error during loading the hints box
   */
  private void loadHintsBox(Pane pane) throws IOException {
    // load hints box
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/instructions.fxml"));
    Pane hintsPane = loader.load();
    pane.getChildren().clear();
    pane.getChildren().add(hintsPane);
  }

}