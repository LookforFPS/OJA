package me.lookforfps.oja.chatcompletion.model.natives.request;

import lombok.Getter;

@Getter
public enum ServiceTier {

    AUTO("auto"),
    DEFAULT("default");

    private String identifier;

    ServiceTier(String identifier) {
        this.identifier = identifier;
    }
}
