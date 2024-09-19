package me.lookforfps.oja.embeddings.usage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usage {

    private Integer prompt_tokens;
    private Integer total_tokens;
}
