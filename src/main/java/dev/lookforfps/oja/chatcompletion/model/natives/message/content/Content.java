package dev.lookforfps.oja.chatcompletion.model.natives.message.content;

import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor
public class Content extends ArrayList<ContentEntry> {

    public static Content createTextContent(String text) {
        Content content = new Content();
        content.add(ContentEntry.createTextEntry(text));
        return content;
    }
    public static Content createImageContent(String imageUrl, String additionalText) {
        Content content = new Content();
        content.add(ContentEntry.createImageEntry(imageUrl));
        content.add(ContentEntry.createTextEntry(additionalText));
        return content;
    }
    public static Content createImageContent(String imageUrl) {
        Content content = new Content();
        content.add(ContentEntry.createImageEntry(imageUrl));
        return content;
    }

    public String getTextContent() {
        TextContent content = (TextContent) this.stream().filter(contentEntry -> contentEntry instanceof TextContent).findFirst().orElse(null);
        if(content != null) {
            return content.getText();
        }
        return null;
    }
    public String getImageContent() {
        ImageContent content = (ImageContent) this.stream().filter(contentEntry -> contentEntry instanceof ImageContent).findFirst().orElse(null);
        if(content != null) {
            return content.getImage_url().getUrl();
        }
        return null;
    }
}
