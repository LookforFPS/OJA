package dev.lookforfps.oja.chatcompletion.model.streaming.choice;

import lombok.Data;
import dev.lookforfps.oja.chatcompletion.model.natives.logprobs.LogProbs;

@Data
public class Choice {

    private String id;
    private Delta delta;
    private LogProbs logprobs;
    private String finish_reason;
    private Integer index;
}
