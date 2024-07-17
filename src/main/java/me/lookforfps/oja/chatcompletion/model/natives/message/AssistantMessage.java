package me.lookforfps.oja.chatcompletion.model.natives.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import me.lookforfps.oja.chatcompletion.model.natives.tools.ToolCall;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AssistantMessage extends Message {

    private String content;
    private String name;
    private List<ToolCall> tool_calls;


    public AssistantMessage(String content, String name, List<ToolCall> tool_calls) {
        super(MessageRole.ASSISTANT);
        this.content = content;
        this.name = name;
        this.tool_calls = tool_calls;
    }
    public AssistantMessage(String content, List<ToolCall> tool_calls) {
        this(content, null, tool_calls);
    }
    public AssistantMessage(String content, String name) {
        this(content, name, null);
    }
    public AssistantMessage(String content) {
        this(content,(String) null);
    }

}
