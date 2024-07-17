package me.lookforfps.oja.chatcompletion.model.natives.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class SystemMessage extends Message {

    private String content;
    private String name;

    public SystemMessage(String content, String name) {
        super(MessageRole.SYSTEM);
        this.content = content;
        this.name = name;
    }
    public SystemMessage(String content) {
        this(content, null);
    }
}
