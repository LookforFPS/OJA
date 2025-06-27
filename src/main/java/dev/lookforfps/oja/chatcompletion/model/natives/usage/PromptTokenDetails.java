package dev.lookforfps.oja.chatcompletion.model.natives.usage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromptTokenDetails {

    private Integer cached_tokens;
    private Integer audio_tokens;

}
