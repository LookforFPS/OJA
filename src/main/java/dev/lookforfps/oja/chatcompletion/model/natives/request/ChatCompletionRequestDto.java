package dev.lookforfps.oja.chatcompletion.model.natives.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import dev.lookforfps.oja.chatcompletion.model.natives.message.Message;
import dev.lookforfps.oja.chatcompletion.model.natives.tools.Tool;
import dev.lookforfps.oja.chatcompletion.model.streaming.StreamOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatCompletionRequestDto {
    private String model;
    private List<Message> messages;
    private Boolean store;
    private String reasoning_effort;
    private Map<String, String> metadata;
    private Float frequency_penalty;
    private HashMap<String, Integer> logit_bias;
    private Boolean logprobs;
    private Integer top_logprobs;
    @Deprecated
    private Integer max_tokens;
    private Integer max_completion_tokens;
    private Integer n;
    private Float presence_penalty;
    private ResponseFormat response_format;
    private Integer seed;
    private String service_tier;
    private List<String> stop;
    private Boolean stream;
    private StreamOptions stream_options;
    private Float temperature;
    private Float top_p;
    private List<Tool> tools;
    private String tool_choice;
    private Boolean parallel_tool_calls;
    private String user;
}
