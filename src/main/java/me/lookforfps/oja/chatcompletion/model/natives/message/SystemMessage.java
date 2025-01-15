package me.lookforfps.oja.chatcompletion.model.natives.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import me.lookforfps.oja.chatcompletion.model.natives.message.content.Content;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class SystemMessage extends Message {

    private Content content;
    private String name;

    public SystemMessage(Content content, String name) {
        super(MessageRole.DEVELOPER);
        this.content = content;
        this.name = name;
    }

    public SystemMessage(String content, String name) {
        this(Content.createTextContent(content), name);
    }

    public SystemMessage(Content content) {
        this(content, null);
    }
    public SystemMessage(String content) {
        this(content, null);
    }
}
