package me.lookforfps.oja.chatcompletion.model.natives.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.lookforfps.oja.chatcompletion.model.natives.usage.Usage;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatCompletionResponse {

    private String id;
    private String object;
    private Integer created;
    private String model;
    private String system_fingerprint;
    private Usage usage;
    private List<Choice> choices;
    private String service_tier;

    public String getTextContent(Integer choiceIndex) {
        return choices.get(choiceIndex).getMessage().asAssistantMessage().getContent().getTextContent();
    }
    public String getTextContent() {
        return getTextContent(0);
    }
}
