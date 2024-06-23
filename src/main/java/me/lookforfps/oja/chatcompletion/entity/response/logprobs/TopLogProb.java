package me.lookforfps.oja.chatcompletion.entity.response.logprobs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopLogProb {
    /**
     * <b>Description: </b>
     * The token.
     */
    private String token;
    /**
     * <b>Description: </b>
     * The log probability of this token, if it is within the top 20 most likely tokens. Otherwise, the value <code>-9999.0</code> is used to signify that the token is very unlikely.
     */
    private Float logprob;
    /**
     * <b>Description: </b>
     * A list of integers representing the UTF-8 bytes representation of the token.
     * Useful in instances where characters are represented by multiple tokens and their byte representations must be combined to generate the correct text representation.
     * Can be <code>null</code> if there is no bytes representation for the token.
     */
    private Byte[] bytes;
}
