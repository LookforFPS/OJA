package me.lookforfps.oja.chatcompletion.model.natives.tools;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class Property {
    private String type;
    private String description;
    @JsonProperty("enum")
    private List<String> enumValues;

    public Property(String type, String description, String[] enumValues) {
        this.type = type;
        this.description = description;
        if(enumValues != null) {
            this.enumValues = Arrays.stream(enumValues).collect(Collectors.toList());
        }
    }
    public Property(String type, String[] enumValues) {
        this(type, null, enumValues);
    }
    public Property(String type, String description) {
        this(type, description, null);
    }

    public static Map.Entry<String, Property> create(String name, String type, String description, String[] enumValues) {
        return new AbstractMap.SimpleEntry<>(name, new Property(type, description, enumValues));
    }
    public static Map.Entry<String, Property> create(String name, String type, String[] enumValues) {
        return new AbstractMap.SimpleEntry<>(name, new Property(type, null, enumValues));
    }
    public static Map.Entry<String, Property> create(String name, String type, String description) {
        return new AbstractMap.SimpleEntry<>(name, new Property(type, description, null));
    }
}
