package dev.lookforfps.oja.moderation.model.input.inputentry;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ImageInputEntry extends InputEntry {

    private ImageURL image_url;

    public ImageInputEntry(ImageURL image_url) {
        super("image_url");
        this.image_url = image_url;
    }
}
