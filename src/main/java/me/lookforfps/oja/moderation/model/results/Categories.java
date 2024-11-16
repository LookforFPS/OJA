package me.lookforfps.oja.moderation.model.results;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Categories {

    private Boolean harassment;
    private Boolean harassment_threatening;
    private Boolean sexual;
    private Boolean sexual_minors;
    private Boolean hate;
    private Boolean hate_threatening;
    private Boolean illicit;
    private Boolean illicit_violent;
    private Boolean selfHarm;
    private Boolean selfHarm_intent;
    private Boolean selfHarm_instructions;
    private Boolean violence;
    private Boolean violence_graphic;

}
