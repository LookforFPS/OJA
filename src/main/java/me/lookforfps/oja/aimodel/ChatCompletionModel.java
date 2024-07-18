package me.lookforfps.oja.aimodel;

import lombok.Getter;

@Getter
public enum ChatCompletionModel {
    /**
     * <b>Description: </b>
     * Our high-intelligence flagship model for complex, multi-step tasks.
     * GPT-4o is cheaper and faster than GPT-4 Turbo.
     */
    GPT_4_O("gpt-4o"),
    /**
     * <b>Description: </b>
     * Our affordable and intelligent small model for fast, lightweight tasks.
     */
    GPT_4_O_MINI("gpt-4o-mini"),
    /**
     * <b>Description: </b>
     * The latest GPT-4 Turbo model with vision capabilities.
     * Vision requests can now use JSON mode and function calling.
     */
    GPT_4_TURBO("gpt-4-turbo"),
    /**
     * <b>Description: </b>
     * Currently points to gpt-4-0613.
     * Snapshot of gpt-4 from June 13th 2023 with improved function calling support.
     */
    GPT_4("gpt-4"),

    /**
     * <b>Description: </b>
     * The latest GPT-3.5 Turbo model with higher accuracy at responding in requested formats and a fix for a bug which caused a text encoding issue for non-English language function calls.
     * Returns a maximum of 4,096 output tokens.
     */
    GPT_3_5_TURBO("gpt-3.5-turbo");

    private final String identifier;

    ChatCompletionModel(String identifier) {
        this.identifier = identifier;
    }
}
