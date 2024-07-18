package me.lookforfps.oja.aimodel;

import lombok.Getter;

@Getter
public enum ModerationModel {
    /**
     * <b>Description: </b>
     * Currently points to text-moderation-007.
     */
    TEXT_MODERATION_LATEST("text-moderation-latest"),
    /**
     * <b>Description: </b>
     * Currently points to text-moderation-007.
     */
    TEXT_MODERATION_STABLE("text-moderation-stable"),
    /**
     * <b>Description: </b>
     * Most capable moderation model across all categories.
     */
    TEXT_MODERATION_007("text-moderation-007");

    private String identifier;

    ModerationModel(String identifier) {
        this.identifier = identifier;
    }
}
