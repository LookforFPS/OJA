package me.lookforfps.oja.aimodel;

import lombok.Getter;

@Getter
public enum EmbeddingModel {
    /**
     * <b>Description: </b>
     * Increased performance over 2nd generation ada embedding model
     */
    TEXT_EMBEDDING_3_SMALL("text-embedding-3-small"),
    /**
     * <b>Description: </b>
     * Most capable embedding model for both english and non-english tasks
     */
    TEXT_EMBEDDING_3_LARGE("text-embedding-3-large"),
    /**
     * <b>Description: </b>
     * Most capable 2nd generation embedding model, replacing 16 first generation models
     */
    TEXT_EMBEDDING_ADA_002("text-embedding-ada-002");

    private final String identifier;

    EmbeddingModel(String identifier) {
        this.identifier = identifier;
    }
}
