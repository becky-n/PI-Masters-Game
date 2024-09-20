package nz.ac.auckland.se206.states;

import java.io.IOException;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.GameStateContext;

/**
 * The Guessing state of the game. Handles the logic for when the player is
 * making a guess about the
 * profession of the characters in the game.
 */
public class Guessing implements GameState {


  /**
   * Constructs a new Guessing state with the given game state context.
   *
   * @param context the context of the game state
   */
  public Guessing(GameStateContext context) {
  }

  /**
   * Handles the event when a rectangle is clicked. Checks if the clicked
   * rectangle is a customer
   * and updates the game state accordingly.
   *
   * @param event       the mouse event triggered by clicking a rectangle
   * @param rectangleId the ID of the clicked rectangle
   * @throws IOException if there is an I/O error
   */
  @Override
  public void handleRectangleClick(MouseEvent event, String rectangleId) throws IOException {
  }

  @Override
  public void handleClueClick(MouseEvent event, String rectangleId) throws IOException {

  }

  /**
   * Handles the event when the guess button is clicked. Since the player has
   * already guessed, it
   * notifies the player.
   *
   * @throws IOException if there is an I/O error
   */
  @Override
  public void handleGuessClick() throws IOException {
    
  }
}
