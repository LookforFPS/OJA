package dev.lookforfps.oja.chatcompletion.model.natives.usage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompletionTokenDetails {

    private Integer reasoning_tokens;
    private Integer audio_tokens;
    private Integer accepted_prediction_tokens;
    private Integer rejected_prediction_tokens;

}
