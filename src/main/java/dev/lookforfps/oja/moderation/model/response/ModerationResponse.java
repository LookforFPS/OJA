package dev.lookforfps.oja.moderation.model.response;

import lombok.*;
import dev.lookforfps.oja.moderation.model.results.Result;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class ModerationResponse {

    @Getter
    @Setter
    private String id;
    @Getter
    @Setter
    private String model;
    private List<Result> results;

    public List<Result> getAllResults() {
        return results;
    }
    public void setAllResults(List<Result> results) {
        this.results = results;
    }
    public Result getResult() {
        return results.get(0);
    }

}
