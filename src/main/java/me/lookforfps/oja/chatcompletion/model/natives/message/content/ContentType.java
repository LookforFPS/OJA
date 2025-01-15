package me.lookforfps.oja.chatcompletion.model.natives.message.content;

public enum ContentType {
    TEXT("text"),
    IMAGE_URL("image_url"),
    REFUSAL("refusal");

    private String identifier;

    ContentType(String identifier) {
        this.identifier = identifier;
    }

    public static ContentType fromIdentifier(String identifier) {
        for (ContentType type : ContentType.values()) {
            if (type.identifier.equals(identifier)) {
                return type;
            }
        }
        return null;
    }
}
