package me.lookforfps.oja.model;

import lombok.Getter;
import me.lookforfps.oja.entity.Model;

import java.util.Arrays;

@Getter
public enum EmbeddingModel {
    /**
     * <b>Description: </b>
     * Increased performance over 2nd generation ada embedding model
     */
    TEXT_EMBEDDING_3_SMALL(new Model("text-embedding-3-small", "Text-Embedding-3 Small")),
    /**
     * <b>Description: </b>
     * Most capable embedding model for both english and non-english tasks
     */
    TEXT_EMBEDDING_3_LARGE(new Model("text-embedding-3-large", "Text-Embedding-3 Large")),
    /**
     * <b>Description: </b>
     * Most capable 2nd generation embedding model, replacing 16 first generation models
     */
    TEXT_EMBEDDING_ADA_002(new Model("text-embedding-ada-002", "Text-Embedding Ada 002"));

    private Model Model;

    EmbeddingModel(Model Model) {
        this.Model = Model;
    }

    public static EmbeddingModel getModelByIdentifier(String identifier) {
        return Arrays.stream(EmbeddingModel.values()).filter(aiModel -> aiModel.getModel().getIdentifier().equals(identifier)).findFirst().orElse(null);
    }

    public static EmbeddingModel getModelByLabel(String name) {
        return Arrays.stream(EmbeddingModel.values()).filter(aiModel -> aiModel.getModel().getLabel().equals(name)).findFirst().orElse(null);
    }
}
