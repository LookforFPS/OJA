package me.lookforfps.oja.chatcompletion.model.natives.tools;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.lookforfps.oja.chatcompletion.model.natives.tools.types.PropertyType;

import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
public class Property {
    private String type;
    private String description;
    @JsonProperty("enum")
    private List<String> enumValues;

    public Property(PropertyType type, String description, String[] enumValues) {
        this.type = type.getIdentifier();
        this.description = description;
        if(enumValues != null) {
            this.enumValues = Arrays.stream(enumValues).toList();
        }
    }
    public Property(PropertyType type, String[] enumValues) {
        this(type, null, enumValues);
    }
    public Property(PropertyType type, String description) {
        this(type, description, null);
    }
}
