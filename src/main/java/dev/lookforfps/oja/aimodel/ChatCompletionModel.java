package dev.lookforfps.oja.aimodel;

import lombok.Getter;

@Getter
public enum ChatCompletionModel {

    O1("o1"),
    O1_MINI("o1-mini"),

    O3("o3"),
    O3_MINI("o3-mini"),

    O4("o4"),
    O4_MINI("o4-mini"),


    GPT_3_5_TURBO("gpt-3.5-turbo"),

    GPT_4_O("gpt-4o"),
    GPT_4_O_MINI("gpt-4o-mini"),

    GPT_4("gpt-4"),
    GPT_4_TURBO("gpt-4-turbo"),

    GPT_4_1("gpt-4.1"),
    GPT_4_1_MINI("gpt-4.1-mini"),
    GPT_4_1_NANO("gpt-4.1-nano"),

    GPT_5("gpt-5"),
    GPT_5_NANO("gpt-5-nano"),
    GPT_5_MINI("gpt-5-mini"),
    GPT_5_CHAT("gpt-5-chat-latest"),

    GPT_5_1("gpt-5.1"),
    GPT_5_1_MINI("gpt-5.1-mini"),
    GPT_5_1_CODEX("gpt-5.1-codex"),
    GPT_5_1_CHAT("gpt-5.1-chat-latest"),

    GPT_5_2("gpt-5.2"),
    GPT_5_2_PRO("gpt-5.2-pro"),

    GPT_5_3_CHAT("gpt-5.3-chat-latest"),

    GPT_5_4("gpt-5.4"),
    GPT_5_4_MINI("gpt-5.4-mini"),
    GPT_5_4_NANO("gpt-5.4-nano"),
    GPT_5_4_PRO("gpt-5.4-pro");

    private final String identifier;

    ChatCompletionModel(String identifier) {
        this.identifier = identifier;
    }
}
