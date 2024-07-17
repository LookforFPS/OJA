package me.lookforfps.oja.chatcompletion.model.natives.message;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Message {

    private String role;

    public Message(MessageRole role) {
        this.role = role.getIdentifier();
    }

    public SystemMessage asSystemMessage() {
        return (SystemMessage) this;
    }
    public AssistantMessage asAssistantMessage() {
        return (AssistantMessage) this;
    }
    public UserMessage asUserMessage() {
        return (UserMessage) this;
    }
    public ToolMessage asToolMessage() {
        return (ToolMessage) this;
    }
}
