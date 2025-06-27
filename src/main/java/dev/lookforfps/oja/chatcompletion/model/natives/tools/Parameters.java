package dev.lookforfps.oja.chatcompletion.model.natives.tools;

import lombok.Data;
import lombok.NoArgsConstructor;
import dev.lookforfps.oja.chatcompletion.model.natives.tools.types.ParameterType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class Parameters {
    private String type;
    private Map<String, Property> properties;
    private List<String> required;

    @SafeVarargs
    public Parameters(ParameterType type, String[] required, Map.Entry<String, Property>... property) {
        this.type = type.getIdentifier();
        if(property.length > 0) {
            this.properties = Arrays.stream(property).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
        if (required.length > 0) {
            this.required = Arrays.stream(required).collect(Collectors.toList());
        }
    }
    @SafeVarargs
    public Parameters(ParameterType type, Map.Entry<String, Property>... property) {
        this(type, new String[]{}, property);
    }
}
