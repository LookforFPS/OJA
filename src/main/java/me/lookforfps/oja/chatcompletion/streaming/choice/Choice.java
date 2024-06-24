package me.lookforfps.oja.chatcompletion.streaming.choice;

import lombok.Data;
import me.lookforfps.oja.chatcompletion.response.logprobs.LogProbs;

@Data
public class Choice {

    private String id;
    private Delta delta;
    private LogProbs logprobs;
    private String finish_reason;
    private Integer index;
}
