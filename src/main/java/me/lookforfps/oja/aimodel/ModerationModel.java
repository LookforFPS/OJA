package me.lookforfps.oja.aimodel;

import lombok.Getter;

@Getter
public enum ModerationModel {

    OMNI_MODERATION_LATEST("omni-moderation-latest"),

    TEXT_MODERATION_LATEST("text-moderation-latest"),
    TEXT_MODERATION_STABLE("text-moderation-stable");

    private String identifier;

    ModerationModel(String identifier) {
        this.identifier = identifier;
    }
}
