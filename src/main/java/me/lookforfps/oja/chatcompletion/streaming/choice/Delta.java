package me.lookforfps.oja.chatcompletion.streaming.choice;

import lombok.Data;
import me.lookforfps.oja.chatcompletion.toolcall.ToolCall;

import java.util.List;

@Data
public class Delta {

    private String role;
    private String content;
    private List<ToolCall> tool_calls;
}
