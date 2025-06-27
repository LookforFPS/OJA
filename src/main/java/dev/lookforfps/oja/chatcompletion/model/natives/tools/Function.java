package dev.lookforfps.oja.chatcompletion.model.natives.tools;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Function {
    private String name;
    private String description;
    private Parameters parameters;
    private Boolean strict;

    public Function(String name, String description, Parameters parameters, Boolean strict) {
        this.name = name;
        this.description = description;
        this.parameters = parameters;
        this.strict = strict;
    }
    public Function(String name, String description, Parameters parameters) {
        this(name, description, parameters, null);
    }
    public Function(String name, Parameters parameters, Boolean strict) {
        this(name, null, parameters, strict);
    }
    public Function(String name, Parameters parameters) {
        this(name, null, parameters, null);
    }
    public Function(String name, String description) {
        this(name, description, null, null);
    }
    public Function(String name) {
        this(name, null, null, null);
    }

}
