package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;

public class MapController {
  private static GameStateContext context = new GameStateContext();

  @FXML
  private Polygon aisleHover;
  @FXML
  private Polygon crimeHover;
  @FXML
  private Polygon lobbyHover;
  @FXML
  private Polygon ballroomHover;

  // Sound effects
  private MediaPlayer buttonClickSound;

  /**
   * Initializes the CrimeController. Sets up the timer, menu navigation, chat,
   * and loads the clue menu and hints box.
   * 
   * @throws IOException if there is an I/O error during initialization
   */
  @FXML
  private void initialize() throws IOException {
    // set hover polys invisible
    aisleHover.setVisible(false);
    crimeHover.setVisible(false);
    lobbyHover.setVisible(false);
    ballroomHover.setVisible(false);

    // Any required initialization code can be placed here
    Media buttonClickMedia = new Media(getClass().getResource("/sounds/click.mp3").toString());
    buttonClickSound = new MediaPlayer(buttonClickMedia);

    // add sound to array
    App.addSound(buttonClickSound);
    App.muteSound();
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
    if (clickedRectangle.getId().equals("ballroom")) {
      ballroomHover.setVisible(true);

    } else if (clickedRectangle.getId().equals("aisle")) {
      aisleHover.setVisible(true);

    } else if (clickedRectangle.getId().equals("lobby")) {
      lobbyHover.setVisible(true);

    } else if (clickedRectangle.getId().equals("crime")) {
      crimeHover.setVisible(true);
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
    if (clickedRectangle.getId().equals("ballroom")) {
      ballroomHover.setVisible(false);

    } else if (clickedRectangle.getId().equals("aisle")) {
      aisleHover.setVisible(false);

    } else if (clickedRectangle.getId().equals("lobby")) {
      lobbyHover.setVisible(false);

    } else if (clickedRectangle.getId().equals("crime")) {
      crimeHover.setVisible(false);
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

    // get clicked rectangle and handle click
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    context.handleClueClick(event, clickedRectangle.getId());

    // set clue scene based on clicked rectangle
    if (clickedRectangle.getId().equals("ballroom")) {
      App.setRoot("ballroom");
    } else if (clickedRectangle.getId().equals("aisle")) {
      App.setRoot("aisle");
    } else if (clickedRectangle.getId().equals("lobby")) {
      App.setRoot("lobby");
    } else if (clickedRectangle.getId().equals("crime")) {
      App.setRoot("crime");
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
    String currentSceneId = App.getCurrentSceneId();

    switch(currentSceneId){
      case "ballroom":
        App.setRoot("ballroom");
        break;
      case "aisle":
        App.setRoot("aisle");
        break;
      case "lobby":
        App.setRoot("lobby");
        break;
      case "crime":
        App.setRoot("crime");
        break;
      case "letter":
        App.setRoot("letter");
        break;
        case "letterCloseUp":
        App.setRoot("letterCloseUp");
        break;
      case "lock":
        App.setRoot("lock");
        break;
      case "safe":
        App.setRoot("safe");
        break;
      case "unlockBox":
        App.setRoot("unlockBox");
        break;
      case "window":
        App.setRoot("window");
        break;
    }
  }
}