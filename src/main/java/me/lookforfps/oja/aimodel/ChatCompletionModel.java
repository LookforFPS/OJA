package me.lookforfps.oja.aimodel;

import lombok.Getter;

@Getter
public enum ChatCompletionModel {
    /**
     * <b>Description: </b>
     * Our most advanced, multimodal flagship model that’s cheaper and faster than GPT-4 Turbo. Currently points to gpt-4o-2024-05-13.
     */
    GPT_4_O(new Model("gpt-4o")),
    /**
     * <b>Description: </b>
     * The latest GPT-4 Turbo model with vision capabilities. Vision requests can now use JSON mode and function calling. Currently points to gpt-4-turbo-2024-04-09.
     */
    GPT_4_TURBO(new Model("gpt-4-turbo")),
    /**
     * <b>Description: </b>
     * Currently points to gpt-4-0613.
     */
    GPT_4(new Model("gpt-4")),

    /**
     * <b>Description: </b>
     * Currently points to gpt-3.5-turbo-0125.
     */
    GPT_3_5_TURBO(new Model("gpt-3.5-turbo"));

    private Model Model;

    ChatCompletionModel(Model Model) {
        this.Model = Model;
    }
}
