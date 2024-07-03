package me.lookforfps.oja.aimodel;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ImageGenerationModel {
    /**
     * <b>Description: </b>
     * The latest DALL·E model released in Nov 2023.
     */
    DALL_E_3(new AIModel("dall-e-3")),
    /**
     * <b>Description: </b>
     * The previous DALL·E model released in Nov 2022. The 2nd iteration of DALL·E with more realistic, accurate, and 4x greater resolution images than the original model.
     */
    DALL_E_2(new AIModel("dall-e-2"));

    private AIModel AIModel;

    ImageGenerationModel(AIModel AIModel) {
        this.AIModel = AIModel;
    }
}
