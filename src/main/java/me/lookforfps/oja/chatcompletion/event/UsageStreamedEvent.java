package me.lookforfps.oja.chatcompletion.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import me.lookforfps.oja.usage.Usage;
import me.lookforfps.oja.chatcompletion.model.streaming.chunk.Chunk;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UsageStreamedEvent extends ChunkStreamedEvent {
    private Usage usage;

    public UsageStreamedEvent(Chunk streamedChunk, Chunk chunkResult, Usage usage) {
        super(streamedChunk, chunkResult);
        this.usage = usage;
    }
}
