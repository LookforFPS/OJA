package me.lookforfps.oja.chatcompletion.model.natives.content;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImageContent extends Content {

    private String type = "image_url";
    private ImageURL image_url;

    public ImageContent(String image_url) {
        this.image_url = new ImageURL(image_url);
    }
}
