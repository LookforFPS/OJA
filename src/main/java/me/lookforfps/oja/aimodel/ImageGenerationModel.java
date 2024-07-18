package me.lookforfps.oja.aimodel;

import lombok.Getter;

@Getter
public enum ImageGenerationModel {
    /**
     * <b>Description: </b>
     * The latest DALL·E model released in Nov 2023.
     */
    DALL_E_3("dall-e-3"),
    /**
     * <b>Description: </b>
     * The previous DALL·E model released in Nov 2022. The 2nd iteration of DALL·E with more realistic, accurate, and 4x greater resolution images than the original model.
     */
    DALL_E_2("dall-e-2");

    private final String identifier;

    ImageGenerationModel(String identifier) {
        this.identifier = identifier;
    }
}
