package me.lookforfps.oja.chatcompletion.entity;

import lombok.Getter;

@Getter
public enum MessageRole {
    USER("user"),
    ASSISTENT("assistent"),
    TOOL("tool"),
    SYSTEM("system");

    private String identifier;

    MessageRole(String identifier) {
        this.identifier = identifier;
    }
}
