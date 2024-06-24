package me.lookforfps.oja.chatcompletion.streaming;

import lombok.Data;
import me.lookforfps.oja.chatcompletion.response.Usage;
import me.lookforfps.oja.chatcompletion.streaming.choice.Choice;

import java.util.List;

@Data
public class ChunkDto {

    private String id;
    private List<Choice> choices;
    private Integer created;
    private String model;
    private String service_tier;
    private String system_fingerprint;
    private String object;
    private Usage usage;
}
