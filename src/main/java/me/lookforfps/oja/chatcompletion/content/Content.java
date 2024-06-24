package me.lookforfps.oja.chatcompletion.content;

public class Content {

    public static TextContent createTextContent(String text) {
        return new TextContent(text);
    }
    public static ImageContent createImageContent(String imageUrl) {
        return new ImageContent(imageUrl);
    }
}