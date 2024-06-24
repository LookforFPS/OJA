package me.lookforfps.oja.chatcompletion.content;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TextContent extends Content {

    private String type = "text";
    private String text;

    public TextContent(String text) {
        this.text = text;
    }
}
