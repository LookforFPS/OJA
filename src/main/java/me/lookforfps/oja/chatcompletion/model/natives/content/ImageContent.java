package me.lookforfps.oja.chatcompletion.model.natives.content;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ImageContent extends Content {

    private String type = "image_url";
    private ImageURL image_url;

    public ImageContent(String image_url) {
        this.image_url = new ImageURL(image_url);
    }
}
