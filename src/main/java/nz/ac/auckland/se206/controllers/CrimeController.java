package nz.ac.auckland.se206.controllers;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.Navigation;
import nz.ac.auckland.se206.TimerManager;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

import org.eclipse.jgit.util.IO;

public class CrimeController {
  private static GameStateContext context = new GameStateContext();

  private static boolean safe = false;
  private static boolean glass = false;
  private static boolean letter = false;

  private AudioClip buttonClickSound;
  private AudioClip twinkleSound;

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
  private void initialize() throws IOException {
    handleClueMenu(clueMenu);
    buttonClickSound = new AudioClip(getClass().getResource("/sounds/click.mp3").toString());
    twinkleSound = new AudioClip(getClass().getResource("/sounds/twinkle.mp3").toString());

    // set hover effects invisible
    safeGlow.setVisible(false);
    glassPileGlow.setVisible(false);
    invitationGlow.setVisible(false);


    try {
      handleClueMenu(clueMenu);
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

  @FXML
  private void onHover(MouseEvent event) throws IOException {

    Rectangle clickedRectangle = (Rectangle) event.getSource();
    context.handleClueClick(event, clickedRectangle.getId());
    if (clickedRectangle.getId().equals("safe")) {
      safeGlow.setVisible(true);
    } else if (clickedRectangle.getId().equals("glass")) {
      glassPileGlow.setVisible(true);
    } else if (clickedRectangle.getId().equals("letter")) {
      invitationGlow.setVisible(true);
    } 
  }

  @FXML
  private void offHover(MouseEvent event) throws IOException {
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    context.handleClueClick(event, clickedRectangle.getId());
    if (clickedRectangle.getId().equals("safe")) {
      safeGlow.setVisible(false);
    } else if (clickedRectangle.getId().equals("glass")) {
      glassPileGlow.setVisible(false);
    } else if (clickedRectangle.getId().equals("letter")) {
      invitationGlow.setVisible(false);
    } 
  }

  @FXML
  private void handleClueClick(MouseEvent event) throws IOException {
    buttonClickSound.play();
    twinkleSound.play();

    Rectangle clickedRectangle = (Rectangle) event.getSource();
    context.handleClueClick(event, clickedRectangle.getId());

    if (clickedRectangle.getId().equals("safe")) {
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
    boolean[] suspects = ChatController.suspectsTalkedTo();
    boolean[] clues = CrimeController.cluesGuessed();
    if (suspects[0] && suspects[1] && suspects[2]) {
      if (clues[0] || clues[1] || clues[2]) {
        context.handleGuessClick();
        App.setRoot("guess");
      }

    }
  }


  @FXML
  public void handleClueMenu(Pane pane) throws IOException {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/clueMenu.fxml"));
    Pane menuPane = loader.load();

    pane.getChildren().clear();
    pane.getChildren().add(menuPane);

  }

  public static void resetClues() {
    safe = false;
    glass = false;
    letter = false;
  }

  public static boolean[] cluesGuessed() {
    boolean[] clues = new boolean[3];

    clues[0] = safe; // represents clue1
    clues[1] = glass; // represents clue2
    clues[2] = letter; // represents clue3
    return clues;
  }
}