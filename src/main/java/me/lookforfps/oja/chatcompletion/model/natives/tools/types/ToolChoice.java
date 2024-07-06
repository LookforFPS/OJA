package me.lookforfps.oja.chatcompletion.model.natives.tools.types;

import lombok.Getter;

@Getter
public enum ToolChoice {
    AUTO("auto"),
    REQUIRED("required"),
    NONE("none");

    private String identifier;

    ToolChoice(String identifier) {
        this.identifier = identifier;
    }
}
