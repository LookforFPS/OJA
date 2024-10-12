package me.lookforfps.oja.chatcompletion.model.natives.message.content;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageURL {
    private String url;
    private String detail;
}
