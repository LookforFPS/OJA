package me.lookforfps.oja.chatcompletion.model.natives.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.lookforfps.oja.usage.Usage;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatCompletionResponse {

    /**
     * <b>Description: </b>
     * A unique identifier for the chat completion.
     */
    private String id;
    /**
     * <b>Description: </b>
     * The object type, which is always <code>chat.completion</code>.
     */
    private String objectType;
    /**
     * <b>Description: </b>
     * The Unix timestamp (in seconds) of when the chat completion was created.
     */
    private Integer created;
    /**
     * <b>Description: </b>
     * The model used for the chat completion.
     */
    private String usedModel;
    /**
     * <b>Description: </b>
     * This fingerprint represents the backend configuration that the model runs with.<br>
     * <br>
     * Can be used in conjunction with the <code>seed</code> request parameter to understand when backend changes have been made that might impact determinism.
     */
    private String systemFingerprint;
    /**
     * <b>Description: </b>
     * Usage statistics for the completion request.
     */
    private Usage usage;
    /**
     * <b>Description: </b>
     * A list of chat completion choices. Can be more than one if <code>n</code> is greater than 1.
     */
    private List<Choice> choices;
    /**
     * <b>Description: </b>
     * The service tier used for processing the request. This field is only included if the <code>service_tier</code> parameter is specified in the request.
     */
    private String serviceTier;

    public String getTextContent(Integer choiceIndex) {
        return choices.get(choiceIndex).getMessage().asAssistantMessage().getContent();
    }
    public String getTextContent() {
        return getTextContent(0);
    }
}
