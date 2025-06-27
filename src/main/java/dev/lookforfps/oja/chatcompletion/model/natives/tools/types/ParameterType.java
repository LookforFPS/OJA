package dev.lookforfps.oja.chatcompletion.model.natives.tools.types;

import lombok.Getter;

@Getter
public enum ParameterType {
    OBJECT("object");

    private String identifier;

    ParameterType(String identifier) {
        this.identifier = identifier;
    }
}
