package me.lookforfps.oja.chatcompletion.model.natives.logprobs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogProbs {

    private List<LogProbContent> content;
}
