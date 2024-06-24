package me.lookforfps.oja.chatcompletion.streaming;

import lombok.Data;
import me.lookforfps.oja.chatcompletion.response.Usage;
import me.lookforfps.oja.chatcompletion.streaming.choice.Choice;

import java.util.List;

@Data
public class Chunk {

    private String id;
    private List<Choice> choices;
    private Integer created;
    private String usedModel;
    private String serviceTier;
    private String systemFingerprint;
    private String object;
    private Usage usage;
}
