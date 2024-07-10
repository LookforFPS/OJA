package me.lookforfps.oja.chatcompletion.model.natives.config;

import lombok.Data;
import me.lookforfps.oja.aimodel.AIModel;
import me.lookforfps.oja.chatcompletion.model.natives.tools.Tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
public class ChatCompletionConfiguration {

    private String apiUrl = "https://api.openai.com/v1/chat/completions";
    private String apiToken;
    /**
     * <b>Description: </b>
     * ID of the model to use.
     */
    private AIModel AIModel;
    /**
     * <b>Description: </b>
     * Specifies if the response, from the AI, should be automatically added to the context.
     * <br>
     * <b>defaultValue: </b><code>true</code>
     */
    private Boolean autoAddAIResponseToContext = true;
    private Integer autoAddAIResponseChoiceIndex = 0;
    /**
     * <b>Description: </b>
     * Number between -2.0 and 2.0.
     * Positive values penalize new tokens based on their existing frequency in the text so far, decreasing the model's likelihood to repeat the same line verbatim.
     */
    private Float frequencyPenalty;
    /**
     * <b>Description: </b>
     * Modify the likelihood of specified tokens appearing in the completion.<br>
     * <br>
     * Accepts a JSON object that maps tokens (specified by their token ID in the tokenizer) to an associated bias value from -100 to 100. Mathematically, the bias is added to the logits generated by the model prior to sampling. The exact effect will vary per model, but values between -1 and 1 should decrease or increase likelihood of selection; values like -100 or 100 should result in a ban or exclusive selection of the relevant token.
     */
    private HashMap<String, Integer> logitBias;
    /**
     * <b>Description: </b>
     * Whether to return log probabilities of the output tokens or not. If true, returns the log probabilities of each output token returned in the <code>content</code> of <code>message</code>.
     */
    private Boolean logprobs;
    /**
     * <b>Description: </b>
     * An integer between 0 and 20 specifying the number of most likely tokens to return at each token position, each with an associated log probability. <code>logprobs</code> must be set to <code>true</code> if this parameter is used.
     */
    private Integer topLogprobs;
    /**
     * <b>Description: </b>
     * The maximum number of tokens that can be generated in the chat completion.
     * The total length of input tokens and generated tokens is limited by the model's context length.
     */
    private Integer maxTokens;
    /**
     * <b>Description: </b>
     * How many chat completion choices to generate for each input message. Note that you will be charged based on the number of generated tokens across all of the choices. Keep <code>n</code> as <code>1</code> to minimize costs.
     */
    private Integer choices;
    /**
     * <b>Description: </b>
     * Number between -2.0 and 2.0.
     * Positive values penalize new tokens based on whether they appear in the text so far, increasing the model's likelihood to talk about new topics.
     */
    private Float presencePenalty;
    /**
     * <b>Description: </b>
     * This feature is in Beta.
     * If specified, our system will make a best effort to sample deterministically, such that repeated requests with the same <code>seed</code> and parameters should return the same result. Determinism is not guaranteed, and you should refer to the <code>system_fingerprint</code> response parameter to monitor changes in the backend.
     */
    private Integer seed;
    /**
     * <b>Description: </b>
     * Specifies the latency tier to use for processing the request.<br>
     * This parameter is relevant for customers subscribed to the scale tier service:
     *
     * <li>If set to 'auto', the system will utilize scale tier credits until they are exhausted.</li>
     * <li>If set to 'default', the request will be processed in the shared cluster.</li>
     * <br>
     * When this parameter is set, the response body will include the service_tier utilized.
     */
    private String serviceTier;
    /**
     * <b>Description: </b>
     * Up to 4 sequences where the API will stop generating further tokens.
     */
    private List<String> stop;
    /**
     * <b>Description: </b>
     * If set, partial message deltas will be sent, like in ChatGPT.
     * Tokens will be sent as data-only <b>server-sent events</b> as they become available, with the stream terminated by a <code>data: [DONE]</code> message.
     */
    private Boolean stream;
    /**
     * <b>Description: </b>
     * Options for streaming response. Only set this when you set <code>stream: true</code>.
     */
    private Boolean includeUsageToStream;
    /**
     * What sampling temperature to use, between 0 and 2.
     * Higher values like 0.8 will make the output more random, while lower values like 0.2 will make it more focused and deterministic.<br>
     * <br>
     * We generally recommend altering this or <code>top_p</code> but not both.
     */
    private Float temperature;
    /**
     * <b>Description: </b>
     * An alternative to sampling with temperature, called nucleus sampling, where the model considers the results of the tokens with top_p probability mass.
     * So 0.1 means only the tokens comprising the top 10% probability mass are considered.<br>
     * <br>
     * We generally recommend altering this or <code>temperature</code> but not both.
     */
    private Float topP;
    /**
     * <b>Description: </b>
     * A list of tools the model may call. Currently, only functions are supported as a tool.
     * Use this to provide a list of functions the model may generate JSON inputs for. A max of 128 functions are supported.
     */
    private List<Tool> tools;
    /**
     * <b>Description: </b>
     * Controls which (if any) tool is called by the model. <code>none</code> means the model will not call any tool and instead generates a message. <code>auto</code> means the model can pick between generating a message or calling one or more tools. <code>required</code> means the model must call one or more tools. Specifying a particular tool via <code>{"type": "function", "function": {"name": "my_function"}}</code> forces the model to call that tool.
     * <br>
     * <code>none</code> is the default when no tools are present. <code>auto</code> is the default if tools are present.
     */
    private String toolChoice;
    /**
     * <b>Description: </b>
     * Whether to enable parallel function calling during tool use.
     */
    private Boolean parallelToolCalls;
    /**
     * <b>Description: </b>
     * A unique identifier representing your end-user, which can help OpenAI to monitor and detect abuse.
     */
    private String user;

    public void addTool(Tool tool) {
        if(tools == null) {
            tools = new ArrayList<Tool>();
        }
        tools.add(tool);
    }
    public void removeTool(Tool tool) {
        tools.remove(tool);
    }
    public void removeTool(int toolIndex) {
        tools.remove(toolIndex);
    }
}
