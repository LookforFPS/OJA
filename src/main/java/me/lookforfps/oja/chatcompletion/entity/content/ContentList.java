package me.lookforfps.oja.chatcompletion.entity.content;

import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor
public class ContentList extends ArrayList<Content> {

    public TextContent getFirstTextContent() {
        return (TextContent) this.stream().filter(content -> content instanceof TextContent).findFirst().orElse(null);
    }

    public ContentList(Content... content) {
        this.addAll(Arrays.stream(content).toList());
    }

    public static ContentList addTextContent(String text) {
        return new ContentList(Content.createTextContent(text));
    }

    public static ContentList addImageWithTextContent(String imageUrl, String text) {
        ContentList contentList = new ContentList();
        contentList.add(Content.createImageContent(imageUrl));
        contentList.add(Content.createTextContent(text));
        return contentList;
    }
    public static ContentList addImageContent(String imageUrl) {
        return new ContentList(Content.createImageContent(imageUrl));
    }
}
