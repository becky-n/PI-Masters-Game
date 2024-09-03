package nz.ac.auckland.se206;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class TimerManager {
    private static TimerManager instance;
    private Timeline timer;
    private int timeRemaining;
    private boolean running;

    private TimerManager() {
        // Initialize the timer with a 1-second interval
        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeRemaining--;
            // Update UI or perform actions based on the timer here
            if (timeRemaining <= 0) {
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
            timeRemaining = durationInSeconds;
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
        return timeRemaining;
    }

    private void handleTimeOut() {
        // Handle timeout logic, such as transitioning to a game over state
    }
}
