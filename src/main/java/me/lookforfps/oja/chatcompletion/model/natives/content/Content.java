package me.lookforfps.oja.chatcompletion.model.natives.content;

public class Content {

    public static TextContent createTextContent(String text) {
        return new TextContent(text);
    }
    public static ImageContent createImageContent(String imageUrl) {
        return new ImageContent(imageUrl);
    }
}