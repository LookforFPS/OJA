package me.lookforfps.oja.chatcompletion.model.streaming.chunk;

import lombok.Data;
import me.lookforfps.oja.chatcompletion.model.natives.usage.Usage;
import me.lookforfps.oja.chatcompletion.model.streaming.choice.Choice;

import java.util.List;

@Data
public class Chunk {

    private String id;
    private List<Choice> choices;
    private Integer created;
    private String model;
    private String service_tier;
    private String system_fingerprint;
    private String object;
    private Usage usage;

    public String getTextContent(Integer choiceIndex) {
        return choices.get(choiceIndex).getDelta().getContent();
    }
    public String getTextContent() {
        return getTextContent(0);
    }
}
