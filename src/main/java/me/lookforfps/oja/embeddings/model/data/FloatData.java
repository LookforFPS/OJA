package me.lookforfps.oja.embeddings.model.data;

import lombok.EqualsAndHashCode;

import java.util.List;

@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class FloatData extends Data {

    private List<Float> embedding;

    public FloatData(List<Float> embedding, Integer index, String object) {
        super(index, object);
        this.embedding = embedding;
    }
}
