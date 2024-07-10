package me.lookforfps.oja.chatcompletion.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import me.lookforfps.oja.chatcompletion.model.streaming.chunk.Chunk;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class StreamFinishedEvent extends ChunkStreamedEvent {
    private String finishReason;

    public StreamFinishedEvent(Chunk streamedChunk, Chunk chunkResult, String finishReason) {
        super(streamedChunk, chunkResult);
        this.finishReason = finishReason;
    }
}
