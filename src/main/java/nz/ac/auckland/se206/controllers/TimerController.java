// package nz.ac.auckland.se206.controllers;

// import javafx.animation.KeyFrame;
// import javafx.animation.Timeline;
// import javafx.beans.property.IntegerProperty;
// import javafx.beans.property.SimpleIntegerProperty;
// import javafx.event.ActionEvent;
// import javafx.event.EventHandler;
// import javafx.util.Duration;

// public class TimerController {
//   private Timeline timeline;
//   private IntegerProperty timeSeconds;

//   public TimerController() {
//     timeSeconds = new SimpleIntegerProperty();
//     timeSeconds.set(0);
//     timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
//       @Override
//       public void handle(ActionEvent event) {
//         timeSeconds.set(timeSeconds.get() + 1);
//       }
//     }));
//     timeline.setCycleCount(Timeline.INDEFINITE);
//   }


//   public void start() {
//     timeline.play();
//   }

//   public void stop() {
//     timeline.stop();
//   }

//   public IntegerProperty timeSecondsProperty() {
//     return timeSeconds;
//   }

// }
