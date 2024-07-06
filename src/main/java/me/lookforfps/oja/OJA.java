package me.lookforfps.oja;

import me.lookforfps.oja.chatcompletion.ChatCompletion;
import me.lookforfps.oja.aimodel.AIModel;
import me.lookforfps.oja.chatcompletion.model.natives.config.ChatCompletionConfiguration;

public class OJA {

    public static ChatCompletion createChatCompletion(String apiToken, AIModel AIModel, ChatCompletionConfiguration configuration) {
        if(configuration==null) {
            configuration = new ChatCompletionConfiguration();
        }
        configuration.setApiToken(apiToken);
        configuration.setAIModel(AIModel);
        return new ChatCompletion(configuration);
    }
    public static ChatCompletion createChatCompletion(String apiToken, String modelIdentifier, ChatCompletionConfiguration configuration) {
        return createChatCompletion(apiToken, new AIModel(modelIdentifier), configuration);
    }
    public static ChatCompletion createChatCompletion(String apiToken, AIModel AIModel) {
        return createChatCompletion(apiToken, AIModel, null);
    }
    public static ChatCompletion createChatCompletion(String apiToken, String modelIdentifier) {
        return createChatCompletion(apiToken, new AIModel(modelIdentifier));
    }
}
