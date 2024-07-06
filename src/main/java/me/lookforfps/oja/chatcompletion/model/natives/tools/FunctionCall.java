package me.lookforfps.oja.chatcompletion.model.natives.tools;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FunctionCall {
    private String name;
    private String arguments;
}
