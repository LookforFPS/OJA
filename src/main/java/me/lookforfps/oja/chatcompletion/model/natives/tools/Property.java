package me.lookforfps.oja.chatcompletion.model.natives.tools;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.lookforfps.oja.chatcompletion.model.natives.tools.types.PropertyType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    public static Map.Entry<String, Property> create(String name, PropertyType type, String description, String[] enumValues) {
        return Map.entry(name, new Property(type, description, enumValues));
    }
    public static Map.Entry<String, Property> create(String name, PropertyType type, String[] enumValues) {
        return Map.entry(name, new Property(type, null, enumValues));
    }
    public static Map.Entry<String, Property> create(String name, PropertyType type, String description) {
        return Map.entry(name, new Property(type, description, null));
    }
}
