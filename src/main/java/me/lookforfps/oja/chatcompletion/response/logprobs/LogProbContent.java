package me.lookforfps.oja.chatcompletion.response.logprobs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogProbContent {
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
    /**
     * <b>Description: </b>
     * List of the most likely tokens and their log probability, at this token position. In rare cases, there may be fewer than the number of requested <code>top_logprobs</code> returned.
     */
    private List<TopLogProb> top_logprobs;
}
