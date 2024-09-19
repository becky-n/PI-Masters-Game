package nz.ac.auckland.se206;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.ImageCursor;
import javafx.scene.image.Image;
import javafx.util.Duration;
import nz.ac.auckland.se206.controllers.ChatController;
import nz.ac.auckland.se206.controllers.CrimeController;
import nz.ac.auckland.se206.controllers.GuessController;
import java.io.IOException;

public class TimerManager {
  private static TimerManager instance;
  private GameStateContext context = new GameStateContext();
  private Timeline timer;
  private IntegerProperty timeRemaining = new SimpleIntegerProperty();
  private boolean running;

  private TimerManager() {
    // Initialize the timer with a 1-second interval
    timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
      timeRemaining.set(timeRemaining.get() - 1);

      if (GuessController.inGuessingState()) {
        if (timeRemaining.get() <= 0) {
          timer.stop();
          try {
            handleTimeOut();
          } catch (IOException e) {
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
          }
          App.setRoot("guess");
          TimerManager.getInstance().reset(60);
          context.setState(context.getGuessingState());
        } catch (IOException e) {
          e.printStackTrace();
        }

      }
    }));
    timer.setCycleCount(Timeline.INDEFINITE);
  }

  public static synchronized TimerManager getInstance() {
    if (instance == null) {
      instance = new TimerManager();
    }
    return instance;
  }

  public void start(int durationInSeconds) {
    if (!running) {
      timeRemaining.set(durationInSeconds);
      running = true;
      timer.play();
    }
  }

  public void stop() {
    if (running) {
      running = false;
      timer.pause();
    }
  }

  public int getTimeRemaining() {
    return timeRemaining.get();
  }

  public IntegerProperty timeRemainingProperty() {
    return timeRemaining;
  }

  private void handleTimeOut() throws IOException {
    App.fadeScenes("timesUp");
  }

  public boolean isRunning() {
    return running;
  }

  public void reset(int durationInSeconds) {
    stop();
    timeRemaining.set(durationInSeconds);
    start(durationInSeconds);
  }

}
