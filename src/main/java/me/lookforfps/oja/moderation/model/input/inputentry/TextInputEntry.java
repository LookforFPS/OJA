package me.lookforfps.oja.moderation.model.input.inputentry;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class TextInputEntry extends InputEntry {

    private String text;

    public TextInputEntry(String text) {
        super("text");
        this.text = text;
    }
}
