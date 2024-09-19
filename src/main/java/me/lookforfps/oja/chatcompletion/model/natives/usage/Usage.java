package me.lookforfps.oja.chatcompletion.model.natives.usage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usage {

    private Integer prompt_tokens;
    private Integer completion_tokens;
    private Integer total_tokens;
    private CompletionTokenDetails completion_tokens_details;
}
