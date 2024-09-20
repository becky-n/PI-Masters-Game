package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.Navigation;

/**
 * Controller class for the Lobby scene.
 * Manages interactions and updates for the Lobby view, including handling the
 * timer,
 * navigating menus, and updating hints.
 */
public class LobbyController {

  @FXML
  private MenuButton menuButton;
  @FXML
  private Label timerLabel;
  @FXML
  private Pane chatPane;
  @FXML
  private Pane clueMenu;

  @FXML
  private Pane instructionsPane;

  private AudioClip buttonClickSound;

  /**
   * Initializes the LobbyController. Sets up the timer, menu navigation, chat,
   * and loads the clue menu and hints box.
   * 
   * @throws IOException if there is an I/O error during initialization
   */
  @FXML
  private void initialize() throws IOException {
    // Load the clue menu
    App.handleClueMenu(clueMenu);

    // Load the hints box
    App.loadHintsBox(instructionsPane);

    buttonClickSound = new AudioClip(getClass().getResource("/sounds/click.mp3").toString());

    // Initialize the controller
    Navigation nav = new Navigation();
    nav.setMenu(menuButton);

    // load the chat
    App.openChat("Jesin", chatPane);

    App.timer(timerLabel);
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

}