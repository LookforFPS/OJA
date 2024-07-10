package me.lookforfps.oja.chatcompletion.model.natives.request;

import lombok.Getter;

@Getter
public enum ResponseType {
    TEXT("text"),
    JSON_OBJECT("json_object");

    private String identifier;

    ResponseType(String identifier) {
        this.identifier = identifier;
    }
}
