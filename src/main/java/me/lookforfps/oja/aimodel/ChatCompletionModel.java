package me.lookforfps.oja.aimodel;

import lombok.Getter;

@Getter
public enum ChatCompletionModel {

    O1("o1"),
    O1_MINI("o1-mini"),

    GPT_4_O("gpt-4o"),
    GPT_4_O_MINI("gpt-4o-mini"),
    GPT_4_TURBO("gpt-4-turbo"),
    GPT_4("gpt-4"),

    GPT_3_5_TURBO("gpt-3.5-turbo");

    private final String identifier;

    ChatCompletionModel(String identifier) {
        this.identifier = identifier;
    }
}
