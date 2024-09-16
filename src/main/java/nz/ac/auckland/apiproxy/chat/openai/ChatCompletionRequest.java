package nz.ac.auckland.apiproxy.chat.openai;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import nz.ac.auckland.apiproxy.config.ApiProxyConfig;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.apiproxy.service.EndPoints;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class ChatCompletionRequest {

  public enum Model {
    GPT_3_5_TURBO("gpt-3.5-turbo"),
    GPT_4("gpt-4"),
    GPT_4o("gpt-4o");

    private final String modelName;

    Model(String modelName) {
      this.modelName = modelName;
    }

    public String getModelName() {
      return modelName;
    }
  }

  private static final int NOT_SET = -1;

  private ApiProxyConfig config;

  // OpenAI required parameters
  private ArrayList<ChatMessage> messages = new ArrayList<>();

  // Optional parameters
  private int maxTokens = NOT_SET;
  private double temperature = NOT_SET;
  private double topP = NOT_SET;
  private int n = NOT_SET;

  public ChatCompletionRequest(ApiProxyConfig config) {
    this.config = config;
  }

  public ChatCompletionRequest addMessage(String role, String content) {
    return addMessage(new ChatMessage(role, content));
  }

  public ChatCompletionRequest addMessage(ChatMessage message) {
    messages.add(message);
    return this;
  }

  public ChatCompletionRequest setMaxTokens(int maxTokens) {
    if (maxTokens < 1) {
      throw new IllegalArgumentException(
          "'max_tokens' must be at least 1, but was given " + maxTokens);
    }
    this.maxTokens = maxTokens;
    return this;
  }

  public ChatCompletionRequest setTemperature(double temperature) {
    if (temperature < 0 || temperature > 2) {
      throw new IllegalArgumentException(
          "'temperature' must be between 0 and 2 inclusive, but was given " + temperature);
    }
    this.temperature = temperature;
    return this;
  }

  public ChatCompletionRequest setTopP(double topP) {
    if (topP < 0 || topP > 2) {
      throw new IllegalArgumentException(
          "'top_p' must be between 0 and 1 inclusive, but was given " + topP);
    }
    this.topP = topP;
    return this;
  }

  public ChatCompletionRequest setN(int n) {
    if (n < 1) {
      throw new IllegalArgumentException("'n' must be at least 1, but was given " + n);
    }
    this.n = n;
    return this;
  }

  @SuppressWarnings("resource")
  public ChatCompletionResult execute() throws ApiProxyException {
    try {
      JsonArrayBuilder jsonMessages = Json.createArrayBuilder();
      for (ChatMessage message : messages) {
        jsonMessages.add(
            Json.createObjectBuilder() //
                .add("role", message.getRole()) //
                .add("content", message.getContent()));
      }

      Model model = Model.GPT_4o; 
      System.out.println("Model: " + model.getModelName());
      JsonObjectBuilder jsonOverallBuilder = Json.createObjectBuilder()
          .add("model", model.getModelName()) // Add the selected model here
          .add("messages", jsonMessages);

      jsonOverallBuilder.add("access_token", config.getApiKey()).add("email", config.getEmail());

      if (maxTokens != NOT_SET) {
        jsonOverallBuilder.add("max_tokens", maxTokens);
      }

      if (temperature > NOT_SET) {
        jsonOverallBuilder.add("temperature", temperature);
      }

      if (topP > NOT_SET) {
        jsonOverallBuilder.add("top_p", topP);
      }

      if (n != NOT_SET) {
        jsonOverallBuilder.add("n", n);
      }

      CloseableHttpClient client = HttpClients.createDefault();

      ResponseChatCompletionViaProxy responseChat = null;
      JsonObject value = jsonOverallBuilder.build();

      HttpPost httpPost = new HttpPost(EndPoints.PROXY_OPENAI_CHAT_COMPLETIONS);
      httpPost.setHeader("Content-Type", "application/json");
      httpPost.setHeader("Accept", "application/json");
      httpPost.setEntity(new StringEntity(value.toString()));
      ObjectMapper mapperApiMapper = new ObjectMapper();

      responseChat = (ResponseChatCompletionViaProxy) client.execute(
          httpPost,
          httpResponse -> mapperApiMapper.readValue(
              httpResponse.getEntity().getContent(),
              ResponseChatCompletionViaProxy.class));

      if (!responseChat.success && responseChat.code != 0) {
        throw new ApiProxyException("Problem calling API: " + responseChat.message);
      }
      return new ChatCompletionResult(responseChat.chat_completion);

    } catch (Exception e) {
      throw new ApiProxyException("Problem calling API: " + e.getMessage());
    }
  }
}
