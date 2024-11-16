package me.lookforfps.oja.moderation.model.results;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {

    private Boolean flagged;
    private Categories categories;
    private CategoryScores categoryScores;
    private CategoryAppliedInputTypes categoryAppliedInputTypes;

}
