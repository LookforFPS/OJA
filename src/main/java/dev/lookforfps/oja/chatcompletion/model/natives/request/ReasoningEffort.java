package dev.lookforfps.oja.chatcompletion.model.natives.request;

import lombok.Getter;

@Getter
public enum ReasoningEffort {
    NONE("none"),
    MINIMAL("minimal"),
    LOW("low"),
    MEDIUM("medium"),
    HIGH("high"),
    XHIGH("xhigh");

    private String identifier;

    ReasoningEffort(String identifier) {
        this.identifier = identifier;
    }
}
