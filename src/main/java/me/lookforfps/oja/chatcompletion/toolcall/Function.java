package me.lookforfps.oja.chatcompletion.toolcall;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Function {
    private String name;
    private String arguments;
}