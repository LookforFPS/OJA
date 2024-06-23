package me.lookforfps.oja.model;

import lombok.Getter;
import me.lookforfps.oja.entity.Model;

import java.util.Arrays;

@Getter
public enum ModerationModel {
    /**
     * <b>Description: </b>
     * Currently points to text-moderation-007.
     */
    TEXT_MODERATION_LATEST(new Model("text-moderation-latest", "Text-Moderation Latest")),
    /**
     * <b>Description: </b>
     * Currently points to text-moderation-007.
     */
    TEXT_MODERATION_STABLE(new Model("text-moderation-stable", "Text-Moderation Stable")),
    /**
     * <b>Description: </b>
     * Most capable moderation model across all categories.
     */
    TEXT_MODERATION_007(new Model("text-moderation-007", "Text-Moderation 007"));

    private Model Model;

    ModerationModel(Model Model) {
        this.Model = Model;
    }

    public static ModerationModel getModelByIdentifier(String identifier) {
        return Arrays.stream(ModerationModel.values()).filter(aiModel -> aiModel.getModel().getIdentifier().equals(identifier)).findFirst().orElse(null);
    }

    public static ModerationModel getModelByLabel(String name) {
        return Arrays.stream(ModerationModel.values()).filter(aiModel -> aiModel.getModel().getLabel().equals(name)).findFirst().orElse(null);
    }
}
