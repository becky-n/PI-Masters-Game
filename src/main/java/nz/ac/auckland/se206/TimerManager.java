package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Duration;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.controllers.ChatController;
import nz.ac.auckland.se206.controllers.CrimeController;
import nz.ac.auckland.se206.controllers.GuessController;
import nz.ac.auckland.se206.controllers.TabletController;

/**
 * Manages the game timer.
 */
public class TimerManager {

  // Static Fields
  private static TimerManager instance;
  public static boolean timed = false;

  public static boolean timedUp() {
    return timed;
  }

  /**
   * Returns the TimerManager instance.
   *
   * @return the TimerManager instance
   */
  public static synchronized TimerManager getInstance() {
    if (instance == null) {
      instance = new TimerManager();
    }
    return instance;
  }

  public static void setTabletController(TabletController tabletController) {
    instance.tablet = tabletController;
  }

  private GameStateContext context = new GameStateContext();
  private Timeline timer;
  private IntegerProperty timeRemaining = new SimpleIntegerProperty();
  private TabletController tablet;
  private boolean running;

  // Constructor
  private TimerManager() {
    // Initialize the timer with a 1-second interval
    timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
      timeRemaining.set(timeRemaining.get() - 1);

      if (timeRemaining.get() <= 30) {
        timed = true;
      }

      if (GuessController.inGuessingState()) {
        if (timeRemaining.get() <= 0) {
          timer.stop();
          if(!GuessController.isGuessMade()){
            try {
              handleTimeOut();
            } catch ( IOException e) {
              e.printStackTrace();
            }
            return;
          }
          try {
            handleTimeOutGuess();
          } catch (ApiProxyException | IOException e) {
            e.printStackTrace();
          }
        }
      }

      if (timeRemaining.get() <= 0 && !GuessController.inGuessingState()) {
        timer.stop();
        boolean[] suspects = ChatController.suspectsTalkedTo();
        boolean[] clues = CrimeController.cluesGuessed();
        boolean allCluesNotFound = true;

        for (boolean clue : clues) {
          if (clue) {
            allCluesNotFound = false;
            break;
          }
        }
        try {
          if (!suspects[0] || !suspects[1] || !suspects[2] || allCluesNotFound) {
            handleTimeOut();
          } else {
            App.setRoot("guess");
            TimerManager.getInstance().reset(60);
            context.setState(context.getGuessingState());
          }

        } catch (IOException e) {
          e.printStackTrace();
        }

      }
    }));
    timer.setCycleCount(Timeline.INDEFINITE);
  }

  /**
   * Starts the timer with the specified duration.
   *
   * @param durationInSeconds the duration in seconds
   */
  public void start(int durationInSeconds) {
    if (!running) {
      timeRemaining.set(durationInSeconds);
      running = true;
      timer.play();
    }
  }

  /**
   * Stops the timer.
   */
  public void stop() {
    if (running) {
      running = false;
      timer.pause();
    }
  }

  /**
   * Returns the time remaining on the timer.
   *
   * @return the time remaining on the timer
   */
  public int getTimeRemaining() {
    return timeRemaining.get();
  }

  /**
   * Returns the time remaining property.
   *
   * @return the time remaining property
   */
  public IntegerProperty timeRemainingProperty() {
    return timeRemaining;
  }

  /**
   * Handles the time out event.
   *
   * @throws IOException if there is an I/O error
   */
  private void handleTimeOut() throws IOException {
    App.fadeScenes("timesUp");
  }

  private void handleTimeOutGuess() throws ApiProxyException, IOException {
    tablet.onSendMessage();
  }

  /**
   * Returns whether the timer is running.
   *
   * @return true if the timer is running, false otherwise
   */
  public boolean isRunning() {
    return running;
  }

  /**
   * Resets the timer with the specified duration.
   *
   * @param durationInSeconds the duration in seconds
   */
  public void reset(int durationInSeconds) {
    stop();
    timeRemaining.set(durationInSeconds);
    start(durationInSeconds);
  }

}
