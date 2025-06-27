package dev.lookforfps.oja.chatcompletion.model.natives.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ToolMessage extends Message {

    private String content;
    private String tool_call_id;

    public ToolMessage(String content, String toolCallId) {
        super(MessageRole.TOOL);
        this.content = content;
        this.tool_call_id = toolCallId;
    }

}
