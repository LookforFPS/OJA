package me.lookforfps.oja.moderation.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.lookforfps.oja.aimodel.ModerationModel;
import me.lookforfps.oja.config.Configuration;

@Data
@EqualsAndHashCode(callSuper = true)
public class ModerationConfiguration extends Configuration {

    private String apiUrl = "https://api.openai.com/v1/moderations";

    public void setModel(ModerationModel model) {
        setModel(model.getIdentifier());
    }

}
