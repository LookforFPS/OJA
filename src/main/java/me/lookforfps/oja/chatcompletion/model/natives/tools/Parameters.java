package me.lookforfps.oja.chatcompletion.model.natives.tools;

import lombok.Data;
import lombok.NoArgsConstructor;
import me.lookforfps.oja.chatcompletion.model.natives.tools.types.ParameterType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class Parameters {
    private String type;
    private Map<String, Property> properties;
    private List<String> required;

    public Parameters(ParameterType type, Map<String, Property> properties, String... required) {
        this.type = type.getIdentifier();
        this.properties = properties;
        if (required.length > 0) {
            this.required = Arrays.stream(required).toList();
        }
    }
}
