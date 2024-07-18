package me.lookforfps.oja.chatcompletion.model.natives.request;

import lombok.Getter;

@Getter
public enum ServiceTier {
    /**
     * <b>Description: </b>
     * The system will utilize scale tier credits until they are exhausted.
     */
    AUTO("auto"),
    /**
     * <b>Description: </b>
     * The request will be processed in the shared cluster.
     */
    DEFAULT("default");

    private String identifier;

    ServiceTier(String identifier) {
        this.identifier = identifier;
    }
}
