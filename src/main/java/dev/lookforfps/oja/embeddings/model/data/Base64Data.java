package dev.lookforfps.oja.embeddings.model.data;

import lombok.EqualsAndHashCode;

@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Base64Data extends Data {

    private String embedding;

    public Base64Data(String embedding, Integer index, String object) {
        super(index, object);
        this.embedding = embedding;
    }

}
