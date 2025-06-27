package dev.lookforfps.oja.chatcompletion.model.natives.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseFormat {
    private String type;
    private String json_schema;

    public ResponseFormat(String type, String jsonSchema) {
        this.type = type;
        this.json_schema = jsonSchema;
    }
    public ResponseFormat(String type) {
        this(type, null);
    }
}
