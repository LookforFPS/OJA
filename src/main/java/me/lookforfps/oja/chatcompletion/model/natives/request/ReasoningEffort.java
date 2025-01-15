package me.lookforfps.oja.chatcompletion.model.natives.request;

import lombok.Getter;

@Getter
public enum ReasoningEffort {
    LOW("low"),
    MEDIUM("medium"),
    HIGH("high");

    private String identifier;

    ReasoningEffort(String identifier) {
        this.identifier = identifier;
    }
}
