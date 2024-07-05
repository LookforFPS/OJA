package me.lookforfps.oja.chatcompletion.model.natives.tools.types;

import lombok.Getter;

@Getter
public enum ToolType {
    FUNCTION("function");

    private String identifier;

    ToolType(String identifier) {
        this.identifier = identifier;
    }
}
