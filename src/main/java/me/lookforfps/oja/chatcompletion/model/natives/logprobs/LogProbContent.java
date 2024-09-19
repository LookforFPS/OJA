package me.lookforfps.oja.chatcompletion.model.natives.logprobs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogProbContent {

    private String token;
    private Float logprob;
    private Byte[] bytes;
    private List<TopLogProb> top_logprobs;
}
