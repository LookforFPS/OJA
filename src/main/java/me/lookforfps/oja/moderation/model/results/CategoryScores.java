package me.lookforfps.oja.moderation.model.results;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryScores {

    private Float harassment;
    private Float harassment_threatening;
    private Float sexual;
    private Float sexual_minors;
    private Float hate;
    private Float hate_threatening;
    private Float illicit;
    private Float illicit_violent;
    private Float selfHarm;
    private Float selfHarm_intent;
    private Float selfHarm_instructions;
    private Float violence;
    private Float violence_graphic;
}
