package dev.lookforfps.oja.chatcompletion.model.natives.tools;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToolCall {
    private Integer index;
    private String id;
    private String type;
    private FunctionCall function;
}
