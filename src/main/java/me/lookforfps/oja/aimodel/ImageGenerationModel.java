package me.lookforfps.oja.aimodel;

import lombok.Getter;

@Getter
public enum ImageGenerationModel {

    DALL_E_3("dall-e-3"),
    DALL_E_2("dall-e-2");

    private final String identifier;

    ImageGenerationModel(String identifier) {
        this.identifier = identifier;
    }
}
