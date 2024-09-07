package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import nz.ac.auckland.se206.controllers.ChatController;
import nz.ac.auckland.se206.speech.FreeTextToSpeech;

/**
 * This is the entry point of the JavaFX application. This class initializes and
 * runs the JavaFX
 * application.
 */
public class App extends Application {

  private static Scene scene;

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
  }

  private void handleWindowClose(WindowEvent event) {
    FreeTextToSpeech.deallocateSynthesizer();
  }
}
