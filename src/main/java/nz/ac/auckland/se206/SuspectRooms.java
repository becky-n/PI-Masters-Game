package nz.ac.auckland.se206;

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

/**
 * Base class for common room controller logic.
 * Manages shared interactions such as the timer, menus, sounds, and map clicks.
 */
public abstract class SuspectRooms extends MapRooms {

  @FXML
  protected MenuButton menuButton;
  @FXML
  protected Label timerLabel;
  @FXML
  protected Pane chatPane;
  @FXML
  protected Pane clueMenu;
  @FXML
  protected Pane mapPane;
  @FXML
  protected Pane instructionsPane;
  @FXML
  protected Circle redCircle;
  @FXML
  protected ImageView clock;
  @FXML
  protected Pane mutePane;
  @FXML
  protected ImageView mapClose;

  protected MediaPlayer buttonClickSound;

  /**
   * Initializes the controller with shared setup logic.
   * This method should be called by child classes with specific room settings.
   *
   * @param chatName The name of the character associated with the chat.
   * @throws IOException if there is an I/O error during initialization.
   */
  protected void initializeRoom(String chatName) throws IOException {
    // set up audios
    Media buttonClickMedia = new Media(getClass().getResource("/sounds/click.mp3").toString());
    buttonClickSound = new MediaPlayer(buttonClickMedia);
    ArrayList<MediaPlayer> sounds = new ArrayList<>();
    sounds.add(buttonClickSound);

    // load the chat for the room
    App.openChat(chatName, chatPane);

    // initialize the controller with shared components
    App.intialiseControllers(clueMenu, redCircle, clock, instructionsPane, sounds,
        mutePane, timerLabel, mapClose);
  }

  /**
   * Handles the guess button click event.
   *
   * @param event the action event triggered by clicking the guess button.
   * @throws IOException if there is an I/O error.
   */
  @FXML
  protected void onHandleGuessClick(ActionEvent event) throws IOException {
    buttonClickSound.seek(javafx.util.Duration.ZERO);
    buttonClickSound.play();
    App.guessClick();
  }

  /**
   * Handles the map click event.
   *
   * @param event the MouseEvent that triggered this handler.
   * @throws IOException if an I/O error occurs during the map loading process.
   */
  @FXML
  protected void handleMapClick(MouseEvent event) throws IOException {
    buttonClickSound.seek(javafx.util.Duration.ZERO);
    buttonClickSound.play();
    App.handleMapClickSuspect(mapPane, this, chatPane);
  }

  /**
   * Handles the back button click event for the map.
   */
  @Override
  public void onMapBack() {
    App.onMapBackSuspect(chatPane, mutePane);
  }
}
