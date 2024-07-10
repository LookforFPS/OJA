package me.lookforfps.oja.chatcompletion.model.natives.tools.types;

import lombok.Getter;

@Getter
public enum PropertyType {
    STRING("string"),
    INTEGER("integer"),
    NUMBER("number"),
    BOOLEAN("boolean");

    private String identifier;

    PropertyType(String identifier) {
        this.identifier = identifier;
    }
}
