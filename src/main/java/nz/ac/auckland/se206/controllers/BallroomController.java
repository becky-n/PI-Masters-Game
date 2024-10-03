package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.shape.Circle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.Navigation;

public class BallroomController {
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
  @FXML
  private Circle redCircle;
  @FXML
  private ImageView clock;

  private AudioClip buttonClickSound;

  /**
   * Controller class for the Ballroom scene.
   * Manages the initialization of the scene, handling button clicks, and updating
   * UI elements.
   */
  @FXML
  private void initialize() throws IOException {
    // Load the clue menu
    App.handleClueMenu(clueMenu);

    // set circle colour for time almost out
    App.setRedCircle(redCircle, clock);

    // Load the hints box
    App.loadHintsBox(instructionsPane);

    buttonClickSound = new AudioClip(getClass().getResource("/sounds/click.mp3").toString());

    // Initialize the controller
    Navigation nav = new Navigation();
    nav.setMenu(menuButton);

    // load the chat
    App.openChat("Andrea", chatPane);

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