package me.lookforfps.oja.embeddings.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.lookforfps.oja.aimodel.EmbeddingModel;
import me.lookforfps.oja.config.Configuration;
import me.lookforfps.oja.embeddings.model.request.EncodingFormat;

@Data
@EqualsAndHashCode(callSuper = true)
public class EmbeddingConfiguration extends Configuration {

    private String apiUrl = "https://api.openai.com/v1/embeddings";

    private EncodingFormat encodingFormat;
    private Integer dimensions;
    private String user;

    public void setModel(EmbeddingModel model) {
        setModel(model.getIdentifier());
    }
}
