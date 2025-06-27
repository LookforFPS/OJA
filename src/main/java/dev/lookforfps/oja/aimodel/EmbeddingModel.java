package dev.lookforfps.oja.aimodel;

import lombok.Getter;

@Getter
public enum EmbeddingModel {

    TEXT_EMBEDDING_3_SMALL("text-embedding-3-small"),
    TEXT_EMBEDDING_3_LARGE("text-embedding-3-large"),
    TEXT_EMBEDDING_ADA_002("text-embedding-ada-002");

    private final String identifier;

    EmbeddingModel(String identifier) {
        this.identifier = identifier;
    }
}
