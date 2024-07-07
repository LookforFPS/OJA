package me.lookforfps.oja.chatcompletion.model.natives.content;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class TextContent extends Content {

    private String type = "text";
    private String text;

    public TextContent(String text) {
        this.text = text;
    }
}
