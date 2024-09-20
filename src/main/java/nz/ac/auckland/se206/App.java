package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import nz.ac.auckland.se206.controllers.ChatController;
import nz.ac.auckland.se206.controllers.GuessController;
import nz.ac.auckland.se206.controllers.TabletController;
import nz.ac.auckland.se206.controllers.TimesUpController;
import nz.ac.auckland.se206.speech.FreeTextToSpeech;

/**
 * This is the entry point of the JavaFX application. This class initializes and
 * runs the JavaFX
 * application.
 */
public class App extends Application {

  private static Scene scene;
  private static String currentSceneId;

  /**
   * The main method that launches the JavaFX application.
   *
   * @param args the command line arguments
   */
  public static void main(final String[] args) {
    launch();
  }

  /**
   * Sets the root of the scene to the specified FXML file.
   *
   * @param fxml the name of the FXML file (without extension)
   * @throws IOException if the FXML file is not found
   */
  public static void setRoot(String fxml) throws IOException {
    currentSceneId = fxml;
    scene.setRoot(loadFxml(fxml));
  }

  /**
   * Loads the FXML file and returns the associated node. The method expects that
   * the file is
   * located in "src/main/resources/fxml".
   *
   * @param fxml the name of the FXML file (without extension)
   * @return the root node of the FXML file
   * @throws IOException if the FXML file is not found
   */
  private static Parent loadFxml(final String fxml) throws IOException {
    return new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml")).load();
  }

  /**
   * Changes the scene to the specified FXML file with a fade-in and fade-out
   * 
   * @param scene
   */
  public static void fadeScenes(String scene) {
    // Get root of the current scene
    Parent oldRoot = getScene().getRoot();

    // Create a fade-out transition for the old scene
    FadeTransition fadeOut = new FadeTransition(javafx.util.Duration.millis(500), oldRoot);
    fadeOut.setFromValue(1.0);
    fadeOut.setToValue(0.0);

    // Set the action to perform after fade-out completes
    fadeOut.setOnFinished(e -> {
      try {
        // Set the new root (new scene)
        setRoot(scene);

        // Get the new root of the scene
        Parent newRoot = getScene().getRoot();

        // Create a fade-in transition for the new scene
        FadeTransition fadeIn = new FadeTransition(javafx.util.Duration.millis(500), newRoot);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        // Start the fade-in transition
        fadeIn.play();
      } catch (IOException ioException) {
        ioException.printStackTrace();
        // Optionally display an error dialog here
      }
    });

    // Start the fade-out transition
    fadeOut.play();
  }

  /**
   * Opens the chat view and sets the student in the chat controller.
   *
   * @param event the mouse event that triggered the method
   * @param name  the suspect to set in the chat controller
   * @throws IOException if the FXML file is not found
   */
  public static void openChat(String name, Pane chatPane)
      throws IOException {

    try {

      // Load the chat view
      FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fxml/chat.fxml"));
      Pane chatContent = fxmlLoader.load();
      ChatController chat = fxmlLoader.getController();

      // Set the suspect in the chat controller
      chat.setSuspect(name);

      // Clear the chat pane and add the chat view
      chatPane.getChildren().clear();
      chatPane.getChildren().add(chatContent);
      chatPane.setVisible(true);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Opens the tablet view and sets the suspect in the tablet controller.
   *
   * @param name     the suspect to set in the tablet controller
   * @param chatPane the pane to which the tablet view should be added
   * @param guess    the guess controller
   * @throws IOException if the FXML file is not found
   */
  public static void openTablet(String name, Pane chatPane, GuessController guess)
      throws IOException {

    try {
      // Load the chat view
      FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fxml/tablet.fxml"));
      Pane chatContent = fxmlLoader.load();
      TabletController chat = fxmlLoader.getController();

      // Clear the chat pane and add the chat view
      chatPane.getChildren().clear();
      chatPane.getChildren().add(chatContent);
      chatPane.setVisible(true);

      // Set the suspect in the chat controller
      chat.setSuspect(name, guess);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Opens the times up view with the specified information text.
   *
   * @param infoLabelText the text to display in the info label
   * @throws IOException if the FXML file is not found
   */
  public static void openTimesUp(String infoLabelText) throws IOException {
    // Load the TimesUp view and get its controller
    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fxml/timesUp.fxml"));
    Parent timesUpContent = fxmlLoader.load();
    TimesUpController timesUpController = fxmlLoader.getController();

    // Set the text for the info label in the TimesUpController
    if (timesUpController != null) {
      timesUpController.setInfoLabel(infoLabelText);
    } else {
      System.err.println("TimesUpController is not properly loaded.");
      return;
    }

    // Create a fade-out transition for the current scene
    FadeTransition fadeOut = new FadeTransition(
        javafx.util.Duration.millis(500), getScene().getRoot());
    fadeOut.setFromValue(1.0);
    fadeOut.setToValue(0.0);

    // Set the action to perform after fade-out completes
    fadeOut.setOnFinished(e -> {
      try {
        // Set the new root (new scene)
        getScene().setRoot(timesUpContent);

        // Create a fade-in transition for the new scene
        FadeTransition fadeIn = new FadeTransition(
            javafx.util.Duration.millis(500),
            getScene().getRoot());
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        // Start the fade-in transition
        fadeIn.play();
      } catch (Exception ex) {
        ex.printStackTrace();
        // Optionally display an error dialog here
      }
    });

    // Start the fade-out transition
    fadeOut.play();
  }

  /**
   * This method is invoked when the application starts. It loads and shows the
   * "menu" scene.
   *
   * @param stage the primary stage of the application
   * @throws IOException if the "src/main/resources/fxml/menu.fxml" file is not
   *                     found
   */
  @Override
  public void start(final Stage stage) throws IOException {
    Parent root = loadFxml("menu");
    scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
    stage.setOnCloseRequest(event -> handleWindowClose(event));
    root.requestFocus();
    Image image = new Image(getClass().getResourceAsStream("/images/magnifying.png"));
    scene.setCursor(new ImageCursor(image));
  }

  private void handleWindowClose(WindowEvent event) {
    FreeTextToSpeech.deallocateSynthesizer();
  }

  public static String getCurrentSceneId() {
    return currentSceneId;
  }

  public static Scene getScene() {
    return scene;
  }
}
