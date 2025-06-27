package dev.lookforfps.oja.chatcompletion.model.natives.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import dev.lookforfps.oja.chatcompletion.model.natives.message.content.Content;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UserMessage extends Message {

    private Content content;
    private String name;

    public UserMessage(Content content, String name) {
        super(MessageRole.USER);
        this.content = content;
        this.name = name;
    }
    public UserMessage(String content, String name) {
        this(Content.createTextContent(content), name);
    }

    public UserMessage(Content content) {
        this(content, null);
    }
    public UserMessage(String content) {
        this(content, null);
    }

}
