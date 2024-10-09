package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import nz.ac.auckland.se206.SuspectRooms;

/**
 * Controller class for the Aisle scene.
 * Manages interactions and updates for the Aisle view.
 */
public class AisleController extends SuspectRooms {

  /**
   * Initializes the AisleController by setting the chat name to "Gerald".
   *
   * @throws IOException if there is an I/O error during initialization.
   */
  @FXML
  private void initialize() throws IOException {
    initializeRoom("Gerald"); // Specific chat name for Aisle
  }
}
