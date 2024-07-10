package me.lookforfps.oja.chatcompletion.model.streaming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StreamOptions {
    private Boolean include_usage;
}
