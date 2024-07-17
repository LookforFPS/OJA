package me.lookforfps.oja;

import me.lookforfps.oja.chatcompletion.ChatCompletion;
import me.lookforfps.oja.aimodel.Model;
import me.lookforfps.oja.chatcompletion.model.natives.config.ChatCompletionConfiguration;

public class OJA {

    public static ChatCompletion createChatCompletion(String apiToken, Model Model, ChatCompletionConfiguration configuration) {
        if(configuration==null) {
            configuration = new ChatCompletionConfiguration();
        }
        configuration.setApiToken(apiToken);
        configuration.setModel(Model);
        return new ChatCompletion(configuration);
    }
    public static ChatCompletion createChatCompletion(String apiToken, String modelIdentifier, ChatCompletionConfiguration configuration) {
        return createChatCompletion(apiToken, new Model(modelIdentifier), configuration);
    }
    public static ChatCompletion createChatCompletion(String apiToken, Model Model) {
        return createChatCompletion(apiToken, Model, null);
    }
    public static ChatCompletion createChatCompletion(String apiToken, String modelIdentifier) {
        return createChatCompletion(apiToken, new Model(modelIdentifier));
    }
}
