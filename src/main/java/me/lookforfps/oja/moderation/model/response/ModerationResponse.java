package me.lookforfps.oja.moderation.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.lookforfps.oja.moderation.model.results.Result;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModerationResponse {

    private String id;
    private String model;
    private List<Result> results;

    public Result getFirstResult() {
        return results.get(0);
    }

}
