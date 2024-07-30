package me.lookforfps.oja.usage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usage {

    /**
     * <b>Description: </b>
     * Number of tokens in the prompt.
     */
    private Integer prompt_tokens;
    /**
     * <b>Description: </b>
     * Number of tokens in the generated completion.
     */
    private Integer completion_tokens;
    /**
     * <b>Description: </b>
     * Total number of tokens used in the request (prompt + completion).
     */
    private Integer total_tokens;
}
