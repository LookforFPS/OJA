package dev.lookforfps.oja.moderation.model.results;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryAppliedInputTypes {

    private List<String> harassment;
    private List<String> harassment_threatening;
    private List<String> sexual;
    private List<String> sexual_minors;
    private List<String> hate;
    private List<String> hate_threatening;
    private List<String> illicit;
    private List<String> illicit_violent;
    private List<String> selfHarm;
    private List<String> selfHarm_intent;
    private List<String> selfHarm_instructions;
    private List<String> violence;
    private List<String> violence_graphic;
}
