package dev.lookforfps.oja.chatcompletion.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import dev.lookforfps.oja.chatcompletion.model.streaming.chunk.Chunk;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChunkStreamedEvent {
    private Chunk chunk;
    private Chunk chunkResult;
}
