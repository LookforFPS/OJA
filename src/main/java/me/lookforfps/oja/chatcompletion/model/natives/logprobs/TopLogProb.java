package me.lookforfps.oja.chatcompletion.model.natives.logprobs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopLogProb {

    private String token;
    private Float logprob;
    private Byte[] bytes;
}
