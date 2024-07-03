package me.lookforfps.oja.chatcompletion.model.natives.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.lookforfps.oja.chatcompletion.model.natives.message.Message;
import me.lookforfps.oja.model.RequestDto;

import java.util.HashMap;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatCompletionRequestDto extends RequestDto {
    private String model;
    private List<Message> messages;
    private Float frequency_penalty;
    private HashMap<String, Integer> logit_bias;
    private Boolean logprobs;
    private Integer top_logprobs;
    private Integer max_tokens;
    private Integer n;
    private Float presence_penalty;
    private Integer seed;
    private String service_tier;
    private List<String> stop;
    private Boolean stream;
    private Object stream_options;
    private Float temperature;
    private Float top_p;
    private List<Object> tools;
    private Object tool_choice;
    private Boolean parallel_tool_calls;
    private String user;
}
