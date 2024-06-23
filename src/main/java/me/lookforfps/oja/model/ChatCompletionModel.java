package me.lookforfps.oja.model;

import lombok.Getter;
import me.lookforfps.oja.entity.Model;

import java.util.Arrays;

@Getter
public enum ChatCompletionModel {
    /**
     * <b>Description: </b>
     * Our most advanced, multimodal flagship model that’s cheaper and faster than GPT-4 Turbo. Currently points to gpt-4o-2024-05-13.
     */
    GPT_4_O(new Model("gpt-4o", "GPT-4o")),
    /**
     * <b>Description: </b>
     * The latest GPT-4 Turbo model with vision capabilities. Vision requests can now use JSON mode and function calling. Currently points to gpt-4-turbo-2024-04-09.
     */
    GPT_4_TURBO(new Model("gpt-4-turbo", "GPT-4-Turbo")),
    /**
     * <b>Description: </b>
     * Currently points to gpt-4-0613.
     */
    GPT_4(new Model("gpt-4", "GPT-4")),

    /**
     * <b>Description: </b>
     * Currently points to gpt-3.5-turbo-0125.
     */
    GPT_3_5_TURBO(new Model("gpt-3.5-turbo", "GPT-3.5-Turbo"));

    private Model Model;

    ChatCompletionModel(Model Model) {
        this.Model = Model;
    }

    public static ChatCompletionModel getModelByIdentifier(String identifier) {
        return Arrays.stream(ChatCompletionModel.values()).filter(aiModel -> aiModel.getModel().getIdentifier().equals(identifier)).findFirst().orElse(null);
    }

    public static ChatCompletionModel getModelByLabel(String name) {
        return Arrays.stream(ChatCompletionModel.values()).filter(aiModel -> aiModel.getModel().getLabel().equals(name)).findFirst().orElse(null);
    }
}
