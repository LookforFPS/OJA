package me.lookforfps.oja.chatcompletion.model.streaming.chunk;

import lombok.Data;
import me.lookforfps.oja.chatcompletion.model.natives.response.Usage;
import me.lookforfps.oja.chatcompletion.model.streaming.choice.Choice;

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

    public String getTextContent(Integer choiceIndex) {
        return choices.get(choiceIndex).getDelta().getContent();
    }
    public String getTextContent() {
        return getTextContent(0);
    }
}
