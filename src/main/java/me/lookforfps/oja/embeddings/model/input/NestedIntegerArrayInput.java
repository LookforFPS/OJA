package me.lookforfps.oja.embeddings.model.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import me.lookforfps.oja.embeddings.model.request.EmbeddingRequestDto;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class NestedIntegerArrayInput extends EmbeddingRequestDto {

    private List<List<Integer>> input;
}
