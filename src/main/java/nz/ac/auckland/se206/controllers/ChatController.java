package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionRequest;
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionResult;
import nz.ac.auckland.apiproxy.chat.openai.ChatMessage;
import nz.ac.auckland.apiproxy.chat.openai.Choice;
import nz.ac.auckland.apiproxy.config.ApiProxyConfig;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.prompts.PromptEngineering;

/**
 * Controller class for the chat view. Handles user interactions and
 * communication with the GPT model via the API proxy.
 */
public class ChatController {

  private static boolean Andrea = false;
  private static boolean Jesin = false;
  private static boolean Gerald = false;

  /**
   * Returns the suspects that have been talked to.
   *
   * @return an array of booleans indicating which suspects have been talked to
   */
  public static boolean[] suspectsTalkedTo() {
    boolean[] suspects = new boolean[3];
    suspects[0] = Andrea;
    suspects[1] = Jesin;
    suspects[2] = Gerald;
    return suspects;
  }

  /**
   * Resets the suspects that have been talked to.
   */
  public static void resetSuspects() {
    Andrea = false;
    Jesin = false;
    Gerald = false;
  }

  @FXML
  private TextArea txtaChat;
  @FXML
  private TextField txtInput;
  @FXML
  private ImageView btnSend;
  @FXML
  private Label nameLabel;
  @FXML
  private String suspect;

  private ChatCompletionRequest chatCompletionRequest;
  private MediaPlayer buttonClickSound;
  private String str = "";

  /**
   * Initializes the chat view.
   *
   * @throws ApiProxyException if there is an error communicating with the API
   *                           proxy
   */
  @FXML
  public void initialize() throws ApiProxyException {
    // Any required initialization code can be placed here
    Media buttonClickMedia = new Media(getClass().getResource("/sounds/click.mp3").toString());
    buttonClickSound = new MediaPlayer(buttonClickMedia);

    // add sound to array
    App.addSound(buttonClickSound);
    App.muteSound();

    txtInput.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER) { // Check if the key pressed is Enter
        if (suspect.equals("Andrea")) {
          Andrea = true;
        } else if (suspect.equals("Jesin")) {
          Jesin = true;
        } else if (suspect.equals("Gerald")) {
          Gerald = true;
        }
        try {
          onSendMessage(); // Call the method to send the message
        } catch (ApiProxyException | IOException e) {
          e.printStackTrace();
        }
      }
    });

  }

  /**
   * Generates the system prompt based on the suspect.
   *
   * @return the system prompt string
   */
  private String getSystemPrompt() {
    String promptId;
    if (suspect.equals("Jesin")) {
      promptId = "prompts/jesin.txt";
    } else if (suspect.equals("Andrea")) {
      promptId = "prompts/andrea.txt";
    } else {
      promptId = "prompts/gerald.txt";
    }
    return PromptEngineering.getPrompt(promptId, suspect, "");
  }

  /**
   * Sets the suspect for the chat context and initializes the
   * ChatCompletionRequest.
   *
   * @param suspect the suspect to set
   */

  public void setSuspect(String suspect) {
    // Set the suspect
    this.suspect = suspect;
    nameLabel.setText(suspect);

    // loading text
    txtaChat.setText("✨ " + suspect + " is thinking...✨");

    // Initialize the ChatCompletionRequest
    try {
      ApiProxyConfig config = ApiProxyConfig.readConfig();
      chatCompletionRequest = new ChatCompletionRequest(config)
          .setN(1)
          .setTemperature(0.2)
          .setTopP(0.5)
          .setMaxTokens(100);
      runGpt(new ChatMessage("system", getSystemPrompt()));
    } catch (ApiProxyException e) {
      e.printStackTrace();
    }
  }

  /**
   * Appends a chat message to the chat text area.
   *
   * @param msg the chat message to append
   */
  private void appendChatMessage(ChatMessage msg) {
    this.str = msg.getContent();
    animateText();
  }

  /**
   * Runs the GPT model with a given chat message.
   *
   * @param msg the chat message to process
   * @return the response chat message
   * @throws ApiProxyException if there is an error communicating with the API
   *                           proxy
   */
  private void runGpt(ChatMessage msg) throws ApiProxyException {
    if (chatCompletionRequest == null) {
      throw new IllegalStateException("ChatCompletionRequest is not initialized.");
    }

    // Disable input while processing
    txtInput.setDisable(true);
    btnSend.setDisable(true);

    // Add loading message
    txtaChat.clear();
    txtaChat.setText("✨ " + suspect + " is thinking...✨");

    // Create a task to run the GPT model
    Task<ChatMessage> task = new Task<ChatMessage>() {
      // Run the GPT model
      @Override
      protected ChatMessage call() throws ApiProxyException {

        // Add the user message
        chatCompletionRequest.addMessage(msg);
        ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
        Choice result = chatCompletionResult.getChoices().iterator().next();
        chatCompletionRequest.addMessage(result.getChatMessage());
        return result.getChatMessage();
      }

      @Override
      protected void succeeded() {
        txtaChat.clear();

        // Handle the result
        ChatMessage chatMessage = getValue();
        appendChatMessage(chatMessage);

        txtInput.setDisable(false); // Re-enable the input field
        btnSend.setDisable(false); // Re-enable the send button
      }

      // Handle the error
      @Override
      protected void failed() {
        Platform.runLater(
            () -> {
              Throwable exception = getException();
              exception.printStackTrace();
            });
      }
    };

    // Run the task on a background thread
    new Thread(task).start();
  }

  /**
   * Sends a message to the GPT model.
   *
   * @throws ApiProxyException if there is an error communicating with the API
   *                           proxy
   * @throws IOException       if there is an I/O error
   */
  @FXML
  private void onSendMessage() throws ApiProxyException, IOException {
    buttonClickSound.play();
    String message = txtInput.getText().trim();
    if (suspect.equals("Andrea")) {
      Andrea = true;
    } else if (suspect.equals("Jesin")) {
      Jesin = true;
    } else if (suspect.equals("Gerald")) {
      Gerald = true;
    }
    if (message.isEmpty()) {
      // case where user tries to send an empty message
      return;
    }

    str = ""; // Clear the string
    txtInput.clear();
    ChatMessage msg = new ChatMessage("user", message);
    runGpt(msg);
  }

  /**
   * Handles the hover event for the send button.
   */
  @FXML
  private void onHover() {
    btnSend.setOpacity(1);
  }

  /**
   * Handles the hover event for the send button.
   */
  @FXML
  private void offHover() {
    btnSend.setOpacity(0.5);
  }

  /**
   * Animates the text in the chat text area character by character.
   */
  private void animateText() {
    final IntegerProperty i = new SimpleIntegerProperty(0);
    txtaChat.clear(); // Clear the TextArea before starting the animation

    Timeline timeline = new Timeline();
    KeyFrame keyFrame = new KeyFrame(
        Duration.seconds(0.015), // Adjust speed for smoother animation
        event -> {
          if (i.get() < str.length()) {
            txtaChat.appendText(String.valueOf(str.charAt(i.get()))); // Append one character
            i.set(i.get() + 1);
          } else {
            timeline.stop(); // Stop the timeline when all characters are appended
          }
        });
    timeline.getKeyFrames().add(keyFrame);
    timeline.setCycleCount(Animation.INDEFINITE); // Keep the timeline running
    timeline.play();
  }

}
