package nz.ac.auckland.se206;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Duration;

public class TimerManager {
  private static TimerManager instance;
  private Timeline timer;
  private IntegerProperty timeRemaining = new SimpleIntegerProperty();
  private boolean running;

  private TimerManager() {
    // Initialize the timer with a 1-second interval
    timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
      timeRemaining.set(timeRemaining.get() - 1);

      if (timeRemaining.get() <= 0) {
        timer.stop();
        handleTimeOut(); // Handle timeout event
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

  private void handleTimeOut() {
    
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
