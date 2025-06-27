package dev.lookforfps.oja.chatcompletion.model.streaming.choice;

import lombok.Data;
import dev.lookforfps.oja.chatcompletion.model.natives.tools.ToolCall;

import java.util.List;

@Data
public class Delta {

    private String role;
    private String content;
    private String refusal;
    private List<ToolCall> tool_calls;
}
