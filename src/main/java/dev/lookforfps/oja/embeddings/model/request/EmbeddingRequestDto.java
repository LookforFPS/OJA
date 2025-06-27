package dev.lookforfps.oja.embeddings.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmbeddingRequestDto {

    private String model;
    private String encoding_format;
    private Integer dimensions;
    private String user;
}
