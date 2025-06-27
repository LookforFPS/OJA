package dev.lookforfps.oja.embeddings.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import dev.lookforfps.oja.embeddings.model.data.Data;
import dev.lookforfps.oja.embeddings.usage.Usage;

import java.util.List;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class EmbeddingResponse {

    private String model;
    private List<Data> data;
    private String object;
    private Usage usage;

    public Data getData() {
        return data.get(0);
    }
    public Data getData(Integer index) {
        return data.get(index);
    }

    @JsonIgnore
    public List<Float> getFloatEmbedding() {
        return data.get(0).asFloatData().getEmbedding();
    }

    @JsonIgnore
    public String getBase64Embedding() {
        return data.get(0).asBase64Data().getEmbedding();
    }

}
