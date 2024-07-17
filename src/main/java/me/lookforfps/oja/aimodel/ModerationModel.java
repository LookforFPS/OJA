package me.lookforfps.oja.aimodel;

import lombok.Getter;

@Getter
public enum ModerationModel {
    /**
     * <b>Description: </b>
     * Currently points to text-moderation-007.
     */
    TEXT_MODERATION_LATEST(new Model("text-moderation-latest")),
    /**
     * <b>Description: </b>
     * Currently points to text-moderation-007.
     */
    TEXT_MODERATION_STABLE(new Model("text-moderation-stable")),
    /**
     * <b>Description: </b>
     * Most capable moderation model across all categories.
     */
    TEXT_MODERATION_007(new Model("text-moderation-007"));

    private Model Model;

    ModerationModel(Model Model) {
        this.Model = Model;
    }
}
