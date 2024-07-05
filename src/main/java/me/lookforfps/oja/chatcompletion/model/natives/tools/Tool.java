package me.lookforfps.oja.chatcompletion.model.natives.tools;

import lombok.Data;
import lombok.NoArgsConstructor;
import me.lookforfps.oja.chatcompletion.model.natives.tools.types.ToolType;

@Data
@NoArgsConstructor
public class Tool {
    private String type;
    private Function function;

    public Tool(ToolType type, Function function) {
        this.type = type.getIdentifier();
        this.function = function;
    }
}
