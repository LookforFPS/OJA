package me.lookforfps.oja.chatcompletion.model.streaming.choice;

import lombok.Data;
import me.lookforfps.oja.chatcompletion.model.natives.tools.ToolCall;

import java.util.List;

@Data
public class Delta {

    private String role;
    private String content;
    private List<ToolCall> tool_calls;
}
