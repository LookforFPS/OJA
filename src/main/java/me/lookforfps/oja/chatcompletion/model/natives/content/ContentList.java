package me.lookforfps.oja.chatcompletion.model.natives.content;

import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
public class ContentList extends ArrayList<Content> {

    public TextContent getFirstTextContent() {
        return (TextContent) this.stream().filter(content -> content instanceof TextContent).findFirst().orElse(null);
    }

    public ContentList(Content... content) {
        this.addAll(Arrays.stream(content).collect(Collectors.toList()));
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
