package me.lookforfps.oja.chatcompletion.entity.response.logprobs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogProbs {
    /**
     * <b>Description: </b>
     * A list of message content tokens with log probability information.
     */
    private List<LogProbContent> content;
}
