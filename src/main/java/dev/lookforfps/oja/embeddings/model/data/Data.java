package dev.lookforfps.oja.embeddings.model.data;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class Data {

    private Integer index;
    private String object;

    public FloatData asFloatData() {
        return (FloatData) this;
    }

    public Base64Data asBase64Data() {
        return (Base64Data) this;
    }
}
