package me.lookforfps.oja.aimodel;

import lombok.Getter;

@Getter
public enum ModerationModel {
    /**
     * <b>Description: </b>
     * Currently points to text-moderation-007.
     */
    TEXT_MODERATION_LATEST(new AIModel("text-moderation-latest", "Text-Moderation Latest")),
    /**
     * <b>Description: </b>
     * Currently points to text-moderation-007.
     */
    TEXT_MODERATION_STABLE(new AIModel("text-moderation-stable", "Text-Moderation Stable")),
    /**
     * <b>Description: </b>
     * Most capable moderation model across all categories.
     */
    TEXT_MODERATION_007(new AIModel("text-moderation-007", "Text-Moderation 007"));

    private AIModel AIModel;

    ModerationModel(AIModel AIModel) {
        this.AIModel = AIModel;
    }
}
