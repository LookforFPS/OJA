package me.lookforfps.oja.model;

import lombok.Getter;
import me.lookforfps.oja.entity.Model;

import java.util.Arrays;

@Getter
public enum ImageGenerationModel {
    /**
     * <b>Description: </b>
     * The latest DALL·E model released in Nov 2023.
     */
    DALL_E_3(new Model("dall-e-3", "DALL-E-3")),
    /**
     * <b>Description: </b>
     * The previous DALL·E model released in Nov 2022. The 2nd iteration of DALL·E with more realistic, accurate, and 4x greater resolution images than the original model.
     */
    DALL_E_2(new Model("dall-e-2", "DALL-E-2"));

    private Model Model;

    ImageGenerationModel(Model Model) {
        this.Model = Model;
    }

    public static ImageGenerationModel getModelByIdentifier(String identifier) {
        return Arrays.stream(ImageGenerationModel.values()).filter(aiModel -> aiModel.getModel().getIdentifier().equals(identifier)).findFirst().orElse(null);
    }

    public static ImageGenerationModel getModelByLabel(String name) {
        return Arrays.stream(ImageGenerationModel.values()).filter(aiModel -> aiModel.getModel().getLabel().equals(name)).findFirst().orElse(null);
    }
}
