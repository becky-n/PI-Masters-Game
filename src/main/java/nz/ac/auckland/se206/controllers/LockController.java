package nz.ac.auckland.se206.controllers;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.Navigation;
import nz.ac.auckland.se206.TimerManager;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;

import java.io.IOException;

public class LockController {
  private double angle = 0; 
  private List<String> expectedSequence = List.of("left", "right", "left", "right"); 
  private List<String> userSequence = new ArrayList<>();
  public static boolean safeUnlocked = false;
  private AudioClip buttonClickSound;
  private AudioClip twinkleSound;
  private AudioClip keySound;
  
  @FXML
  private ImageView key;

  @FXML
  private MenuButton menuButton;
  @FXML
  private Label timerLabel;
  @FXML
  private Pane clueMenu;
  @FXML
  private Label infoLabel;

  private static GameStateContext context = new GameStateContext();

  @FXML
  public void initialize() {
    
    buttonClickSound = new AudioClip(getClass().getResource("/sounds/click.mp3").toString());
    twinkleSound = new AudioClip(getClass().getResource("/sounds/twinkle.mp3").toString());
    keySound = new AudioClip(getClass().getResource("/sounds/keySound.mp3").toString());
    
    animateText("Try rotating the key, is there a pattern needed to unlock the box?");

    try {
      handleClueMenu(clueMenu);
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Initialize the controller
    Navigation nav = new Navigation();
    nav.setMenu(menuButton);

    TimerManager timerManager = TimerManager.getInstance();

    // Bind the timerLabel to the timeRemaining property
    timerLabel
        .textProperty()
        .bind(
            Bindings.createStringBinding(
                () -> String.format(
                    "%02d:%02d",
                    timerManager.getTimeRemaining() / 60, timerManager.getTimeRemaining() % 60),
                timerManager.timeRemainingProperty()));
   
  }

  @FXML
  private void handleLeftRotate() {
    buttonClickSound.play();
    angle -= 90; // Rotate left by 90 degrees
    rotateImage(15, 75, Rotate.Z_AXIS); 
    trackAction("left");
  }

  @FXML
  private void handleRightRotate() {
    buttonClickSound.play();
    angle += 90; // Rotate right by 90 degrees
    rotateImage(15, 75, Rotate.Z_AXIS); 
    trackAction("right");
  }

  private void rotateImage(double pivotX, double pivotY, javafx.geometry.Point3D axis) {
    Rotate rotate = new Rotate(angle, pivotX, pivotY); 
    rotate.setAxis(axis); 
    key.getTransforms().clear(); 
    key.getTransforms().add(rotate); 
  }

  private void trackAction(String action) {
    userSequence.add(action); 

    // Check if the user sequence matches the expected sequence
    if (userSequence.equals(expectedSequence)) {
      safeUnlocked = true;
       try {
           App.setRoot("unlockBox");
       } catch (IOException e) {
           e.printStackTrace();
       }
    
        userSequence.clear(); 
    }
    
}


public static boolean isBoxUnlocked(){
    if(safeUnlocked){
        return true;
    }else{
        return false;
    }
}

public static void resetLock(){
    safeUnlocked = false;
}

 @FXML
  public static void handleClueMenu(Pane pane) throws IOException {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/clueMenu.fxml"));
    Pane menuPane = loader.load();

    pane.getChildren().clear();
    pane.getChildren().add(menuPane);
  }

  @FXML
  public void onBack() throws IOException {
    App.setRoot("crime");
  }

 /**
   * Handles the guess button click event.
   *
   * @param event the action event triggered by clicking the guess button
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleGuessClick(ActionEvent event) throws IOException {
    buttonClickSound.play();
    boolean[] suspects= ChatController.suspectsTalkedTo();
    boolean[] clues = CrimeController.cluesGuessed();
    if(suspects[0] && suspects[1] && suspects[2]){
      if(clues[0] || clues[1] || clues[2]){
        context.handleGuessClick();
        App.setRoot("guess");
      }
      
    }
  }


  /** Animates the text in the info label. */
  private void animateText(String str) {
    final IntegerProperty i = new SimpleIntegerProperty(0);
    Timeline timeline = new Timeline();
    KeyFrame keyFrame = new KeyFrame(
        Duration.seconds(0.015), // Adjusted for smoother animation
        event -> {
          if (i.get() > str.length()) {
            timeline.stop();
          } else {
            infoLabel.setText(str.substring(0, i.get()));
            i.set(i.get() + 1);
          }
        });
    timeline.getKeyFrames().add(keyFrame);
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
  }
}
