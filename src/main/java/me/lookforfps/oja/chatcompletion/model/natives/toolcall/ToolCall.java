package me.lookforfps.oja.chatcompletion.model.natives.toolcall;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToolCall {
    private String id;
    private String type;
    private Function function;
}
