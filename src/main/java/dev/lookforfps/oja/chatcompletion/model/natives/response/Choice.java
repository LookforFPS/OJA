package dev.lookforfps.oja.chatcompletion.model.natives.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import dev.lookforfps.oja.chatcompletion.model.natives.message.Message;
import dev.lookforfps.oja.chatcompletion.model.natives.logprobs.LogProbs;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Choice {

    private Integer index;
    private Message message;
    private LogProbs logprobs;
    private String finish_reason;

    public String getTextContent() {
        return message.asAssistantMessage().getContent().getTextContent();
    }
}
