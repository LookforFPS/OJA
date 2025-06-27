package dev.lookforfps.oja.chatcompletion.model.natives.message;

import lombok.Getter;

@Getter
public enum MessageRole {
    USER("user"),
    ASSISTANT("assistant"),
    TOOL("tool"),
    SYSTEM("system"),
    DEVELOPER("developer");

    private String identifier;

    MessageRole(String identifier) {
        this.identifier = identifier;
    }

    public static MessageRole fromIdentifier(String identifier) {
        for (MessageRole role : MessageRole.values()) {
            if (role.identifier.equals(identifier)) {
                return role;
            }
        }
        return null;
    }
}
