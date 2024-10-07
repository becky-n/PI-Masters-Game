package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Circle;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.MapRooms;

public class BallroomController extends MapRooms {
  @FXML
  private MenuButton menuButton;
  @FXML
  private Label timerLabel;
  @FXML
  private Pane chatPane;
  @FXML
  private Pane clueMenu;
  @FXML
  private Pane mapPane;
  @FXML
  private Pane instructionsPane;
  @FXML
  private Circle redCircle;
  @FXML
  private ImageView clock;
  @FXML
  private Pane mutePane;
  @FXML
  private ImageView mapClose;

  private MediaPlayer buttonClickSound;

  /**
   * Controller class for the Ballroom scene.
   * Manages the initialization of the scene, handling button clicks, and updating
   * UI elements.
   */
  @FXML
  private void initialize() throws IOException {

    // set up audios
    Media buttonClickMedia = new Media(getClass().getResource("/sounds/click.mp3").toString());
    buttonClickSound = new MediaPlayer(buttonClickMedia);
    ArrayList<MediaPlayer> sounds = new ArrayList<MediaPlayer>();
    sounds.add(buttonClickSound);

    // load the chat
    App.openChat("Andrea", chatPane);

    // initialise the controller
    App.intialiseControllers(
        clueMenu, redCircle, clock, instructionsPane, sounds, mutePane, timerLabel, mapClose);
  }

  /**
   * Handles the guess button click event.
   *
   * @param event the action event triggered by clicking the guess button
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onHandleGuessClick(ActionEvent event) throws IOException {
    buttonClickSound.seek(javafx.util.Duration.ZERO);
    buttonClickSound.play();
    App.guessClick();
  }

  /**
   * Handles the event when the map is clicked.
   * 
   * @param event the MouseEvent that triggered this handler
   * @throws IOException if an I/O error occurs during the map loading process
   */
  @FXML
  private void handleMapClick(MouseEvent event) throws IOException {
    buttonClickSound.seek(javafx.util.Duration.ZERO);
    buttonClickSound.play();

    App.handleMapClickSuspect(mapPane, this, chatPane);
  }

  /**
   * Handles the back button click event.
   */
  @Override
  public void onMapBack() {
    App.onMapBackSuspect(chatPane, mutePane);
  }

}