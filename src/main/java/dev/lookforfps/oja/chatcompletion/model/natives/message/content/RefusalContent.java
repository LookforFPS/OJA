package dev.lookforfps.oja.chatcompletion.model.natives.message.content;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class RefusalContent extends ContentEntry {

    private String type = "refusal";
    private String refusal;

    public RefusalContent(String refusal) {
        this.refusal = refusal;
    }
}