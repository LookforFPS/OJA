package dev.lookforfps.oja.moderation.model.input.inputentry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputEntry {

    private String type;

    public static TextInputEntry createTextEntry(String text) {
        return new TextInputEntry(text);
    }
    public static ImageInputEntry createImageEntry(String imageUrl) {
        return new ImageInputEntry(new ImageURL(imageUrl));
    }

    public TextInputEntry asTextEntry() {
        return (TextInputEntry) this;
    }
    public ImageInputEntry asImageEntry() {
        return (ImageInputEntry) this;
    }
}
