package me.lookforfps.oja.chatcompletion.model.natives.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.lookforfps.oja.chatcompletion.model.natives.message.Message;
import me.lookforfps.oja.chatcompletion.model.natives.tools.Tool;
import me.lookforfps.oja.chatcompletion.model.streaming.StreamOptions;

import java.util.HashMap;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatCompletionRequestDto {
    private String model;
    private List<Message> messages;
    private Float frequency_penalty;
    private HashMap<String, Integer> logit_bias;
    private Boolean logprobs;
    private Integer top_logprobs;
    private Integer max_tokens;
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
