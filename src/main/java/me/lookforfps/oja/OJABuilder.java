package me.lookforfps.oja;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.lookforfps.oja.chatcompletion.ChatCompletion;
import me.lookforfps.oja.entity.Model;
import me.lookforfps.oja.chatcompletion.config.ChatCompletionConfiguration;

@Data
@AllArgsConstructor
public class OJABuilder {

    public static ChatCompletion createChatCompletion(String apiToken, Model model, ChatCompletionConfiguration configuration) {
        if(configuration==null) {
            configuration = new ChatCompletionConfiguration();
        }
        configuration.setApiToken(apiToken);
        configuration.setModel(model);
        return new ChatCompletion(configuration);
    }
    public static ChatCompletion createChatCompletion(String apiToken, Model model) {
        return createChatCompletion(apiToken, model, null);
    }
    public static ChatCompletion createChatCompletion(String apiToken, String modelIdentifier) {
        return createChatCompletion(apiToken, new Model(modelIdentifier, null));
    }
}
