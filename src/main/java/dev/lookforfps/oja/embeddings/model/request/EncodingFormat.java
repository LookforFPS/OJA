package dev.lookforfps.oja.embeddings.model.request;

import lombok.Getter;

@Getter
public enum EncodingFormat {

    FLOAT("float"),
    BASE64("base64");

    private String identifier;

    EncodingFormat(String identifier) {
        this.identifier = identifier;
    }
}
