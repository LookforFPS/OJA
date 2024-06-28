package me.lookforfps.oja.chatcompletion.model.natives.tools;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tool {
    private String type;
    private Function function;
}
