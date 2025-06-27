package dev.lookforfps.oja.chatcompletion.model.natives.message.content;

public class ContentEntry {

    public static TextContent createTextEntry(String text) {
        return new TextContent(text);
    }
    public static ImageContent createImageEntry(String imageUrl, String detail) {
        return new ImageContent(imageUrl, detail);
    }
    public static ImageContent createImageEntry(String imageUrl) {
        return new ImageContent(imageUrl);
    }

    public TextContent asTextContent() {
        return (TextContent) this;
    }

    public ImageContent asImageContent() {
        return (ImageContent) this;
    }
}
