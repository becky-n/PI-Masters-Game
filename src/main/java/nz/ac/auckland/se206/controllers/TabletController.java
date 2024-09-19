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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionRequest;
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionResult;
import nz.ac.auckland.apiproxy.chat.openai.ChatMessage;
import nz.ac.auckland.apiproxy.chat.openai.Choice;
import nz.ac.auckland.apiproxy.config.ApiProxyConfig;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.TimerManager;
import nz.ac.auckland.se206.prompts.PromptEngineering;

/**
 * Controller class for the chat view. Handles user interactions and
 * communication with the GPT model via the API proxy.
 */
public class TabletController {
  public static boolean finished = false;
  private GuessController guess;

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
  @FXML
  private ImageView loading;
  @FXML
  private ImageView background;

  private ChatCompletionRequest chatCompletionRequest;
  private AudioClip buttonClickSound;
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
    background.setImage(new Image("/images/blue.png"));

    loading.setVisible(false);
    buttonClickSound = new AudioClip(getClass().getResource("/sounds/click.mp3").toString());
    

    txtInput.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER) { // Check if the key pressed is Enter
        TimerManager.getInstance().stop();
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
    String guess = "incorrect";
    if (suspect.equals("Gerald")) {
      guess = "correct";
    }
    return PromptEngineering.getPrompt("prompts/tablet.txt", suspect, guess);
  }

  /**
   * Sets the suspect for the chat context and initializes the
   * ChatCompletionRequest.
   *
   * @param suspect the suspect to set
   * @throws IOException
   */

  public void setSuspect(String suspect, GuessController guess) throws IOException {
    this.suspect = suspect;
    this.guess = guess;

    nameLabel.setText(suspect);

    this.str = "Why do you think " + suspect + " stole the ring?";
    txtaChat.appendText(str);

    try {
      ApiProxyConfig config = ApiProxyConfig.readConfig();
      chatCompletionRequest = new ChatCompletionRequest(config)
          .setN(1)
          .setTemperature(0.2)
          .setTopP(0.5)
          .setMaxTokens(100);
      chatCompletionRequest.addMessage(new ChatMessage("system", getSystemPrompt()));
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
    if (str.contains("Well Done!")) {
      background.setImage(new Image("/images/green.png"));
    } else {
      background.setImage(new Image("/images/red.png"));
    }
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

    // hide the input field and send button after 1 message
    txtInput.setVisible(false);
    btnSend.setVisible(false);

    // Add loading message
    txtaChat.clear();
    loading.setVisible(true);

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
        loading.setVisible(false);

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
    }

    str = ""; // Clear the string
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

  /** Animates the text in the motive label. */
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
            guess.togglePlayAgainButton(true);
            timeline.stop(); // Stop the timeline when all characters are appended

          }
        });
    timeline.getKeyFrames().add(keyFrame);
    timeline.setCycleCount(Animation.INDEFINITE); // Keep the timeline running
    timeline.play();

  }

  public static boolean isFinished() {
    return finished;
  }
}
