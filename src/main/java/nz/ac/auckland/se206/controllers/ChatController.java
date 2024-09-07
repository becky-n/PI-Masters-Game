package nz.ac.auckland.se206.controllers;

import java.io.IOException;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.media.AudioClip;
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionRequest;
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionResult;
import nz.ac.auckland.apiproxy.chat.openai.ChatMessage;
import nz.ac.auckland.apiproxy.chat.openai.Choice;
import nz.ac.auckland.apiproxy.config.ApiProxyConfig;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.prompts.PromptEngineering;

/**
 * Controller class for the chat view. Handles user interactions and
 * communication with the GPT model via the API proxy.
 */
public class ChatController {

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
  private AudioClip buttonClickSound;

  /**
   * Initializes the chat view.
   *
   * @throws ApiProxyException if there is an error communicating with the API
   *                           proxy
   */
  @FXML
  public void initialize() throws ApiProxyException {
    // Any required initialization code can be placed here
    buttonClickSound = new AudioClip(getClass().getResource("/sounds/click.mp3").toString());

    txtInput.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER) { // Check if the key pressed is Enter
        try {
          onSendMessage(); // Call the method to send the message
        } catch (ApiProxyException | IOException e) {
          e.printStackTrace();
        }
      }
    });

  }

  /**
   * Generates the system prompt based on the student.
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
    return PromptEngineering.getPrompt(promptId);
  }

  /**
   * Sets the student for the chat context and initializes the
   * ChatCompletionRequest.
   *
   * @param student the student to set
   */

  public void setSuspect(String suspect) {
    this.suspect = suspect;
    nameLabel.setText(suspect);

    txtaChat.setPromptText(suspect + " is thinking...");

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
    txtaChat.appendText(msg.getContent());
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

    // Add loading message
    txtaChat.clear();
    txtaChat.setPromptText(suspect + " is thinking...");

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

        // Handle the result
        ChatMessage chatMessage = getValue();
        appendChatMessage(chatMessage);
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
    if (message.isEmpty()) {
      // case where user tries to send an empty message
      return;
    } else if (txtaChat.getText().trim().isEmpty()) {
      // case where user tries to enter something while system is loading
      return;
    }
    txtInput.clear();
    ChatMessage msg = new ChatMessage("user", message);
    runGpt(msg);
  }

  @FXML
  private void onHover() {
    btnSend.setOpacity(1);
  }

  @FXML
  private void offHover() {
    btnSend.setOpacity(0.5);
  }
}
