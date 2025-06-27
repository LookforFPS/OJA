package dev.lookforfps.oja.embeddings.model.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import dev.lookforfps.oja.embeddings.model.request.EmbeddingRequestDto;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class IntegerArrayInput extends EmbeddingRequestDto {

    private List<Integer> input;
}
