package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import nz.ac.auckland.se206.SuspectRooms;

/**
 * Controller class for the Ballroom scene.
 * Manages interactions and updates for the Ballroom view.
 */
public class LobbyController extends SuspectRooms {

  /**
   * Initializes the BallroomController.
   *
   * @throws IOException if there is an I/O error during initialization.
   */
  @FXML
  private void initialize() throws IOException {
    initializeRoom("Jesin"); // Specific chat name for Ballroom
  }
}
