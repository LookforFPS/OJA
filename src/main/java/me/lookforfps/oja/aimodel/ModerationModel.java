package me.lookforfps.oja.aimodel;

import lombok.Getter;

@Getter
public enum ModerationModel {

    TEXT_MODERATION_LATEST("text-moderation-latest"),
    TEXT_MODERATION_STABLE("text-moderation-stable"),
    TEXT_MODERATION_007("text-moderation-007");

    private String identifier;

    ModerationModel(String identifier) {
        this.identifier = identifier;
    }
}
