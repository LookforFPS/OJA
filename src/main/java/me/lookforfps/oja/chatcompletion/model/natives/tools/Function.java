package me.lookforfps.oja.chatcompletion.model.natives.tools;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Function {
    private String description;
    private String name;
    private Object parameters; // TODO improvements
}
