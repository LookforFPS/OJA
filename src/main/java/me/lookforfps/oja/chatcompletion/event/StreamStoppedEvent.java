package me.lookforfps.oja.chatcompletion.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.lookforfps.oja.chatcompletion.model.streaming.chunk.Chunk;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StreamStoppedEvent {
    private Chunk chunkResult;
}
