package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.MapRooms;

/**
 * Controller class for the Map scene.
 * Manages the initialization of the scene, handling button clicks, and updating
 * UI elements.
 * 
 */
public class MapController {
  private static GameStateContext context = new GameStateContext();
  public static boolean mapOpen = false;

  /**
   * Toggles the map open or closed.
   */
  public static void toggleMapOpen() {
    mapOpen = !mapOpen;
    System.out.println("Map open: " + mapOpen);
  }

  @FXML
  private Polygon aisleHover;
  @FXML
  private Polygon crimeHover;
  @FXML
  private Polygon lobbyHover;
  @FXML
  private Polygon ballroomHover;

  private MediaPlayer buttonClickSound;
  private MapRooms room;
  private Pane pane;

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
    toggleMapOpen();
    // turn off any sounds currently playing
    for (MediaPlayer sound : App.getActiveSounds()) {
      if (sound.getStatus() == MediaPlayer.Status.PLAYING) {
        sound.stop(); // Stop the currently playing sound
      }
    }

    buttonClickSound.seek(javafx.util.Duration.ZERO);
    buttonClickSound.play();

    // get clicked rectangle and handle click
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    context.handleClueClick(event, clickedRectangle.getId());

    // set clue scene based on clicked rectangle
    if (clickedRectangle.getId().equals("ballroom")) {
      // check if already in ballroom
      if (room instanceof BallroomController) {
        toggleMapOpen();
        onBack();
      } else {
        App.setRoot("ballroom");
      }
    } else if (clickedRectangle.getId().equals("aisle")) {
      // check if already in aisle
      if (room instanceof AisleController) {
        toggleMapOpen();
        onBack();
      } else {
        App.setRoot("aisle");
      }
    } else if (clickedRectangle.getId().equals("lobby")) {
      // check if already in lobby
      if (room instanceof LobbyController) {
        toggleMapOpen();
        onBack();
      } else {
        App.setRoot("lobby");
      }
    } else if (clickedRectangle.getId().equals("crime")) {
      // check if already in crime
      if (room instanceof CrimeController) {
        toggleMapOpen();
        onBack();
      } else {
        App.setRoot("crime");
      }
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

    App.unloadMap(pane, room);
  }

  public void setRoom(MapRooms room) {
    this.room = room;
  }

  public void setPane(Pane pane) {
    this.pane = pane;
  }
}