package me.lookforfps.oja.chatcompletion.request;

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

    private String name;

    ServiceTier(String name) {
        this.name = name;
    }
}
