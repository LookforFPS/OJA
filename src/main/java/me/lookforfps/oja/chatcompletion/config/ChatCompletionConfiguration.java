package me.lookforfps.oja.chatcompletion.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.lookforfps.oja.chatcompletion.model.natives.request.ResponseFormat;
import me.lookforfps.oja.chatcompletion.model.natives.request.ResponseType;
import me.lookforfps.oja.chatcompletion.model.natives.request.ServiceTier;
import me.lookforfps.oja.chatcompletion.model.natives.tools.Tool;
import me.lookforfps.oja.config.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ChatCompletionConfiguration extends Configuration {

    private String apiUrl = "https://api.openai.com/v1/chat/completions";

    private Boolean addAIResponseToContext = true;
    private Integer aiResponseChoiceIndex = 0;
    private Float frequencyPenalty;
    private HashMap<String, Integer> logitBias;
    private Boolean logprobs;
    private Integer topLogprobs;
    private Integer maxTokens;
    private Integer choices;
    private Float presencePenalty;
    private Integer seed;
    private ServiceTier serviceTier;
    private List<String> stop;
    private Boolean stream;
    private Boolean includeUsageToStream;
    private ResponseFormat responseFormat;
    private Float temperature;
    private Float topP;
    private List<Tool> tools;
    private String toolChoice;
    private Boolean parallelToolCalls;
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

    public void setResponseFormat(ResponseType responsetype) {
        this.responseFormat = new ResponseFormat(responsetype.getIdentifier());
    }
    public void setResponseFormat(String jsonSchema) {
        this.responseFormat = new ResponseFormat("json_schema", jsonSchema);
    }
}
