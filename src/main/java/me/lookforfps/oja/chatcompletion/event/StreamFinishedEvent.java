package me.lookforfps.oja.chatcompletion.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import me.lookforfps.oja.chatcompletion.model.streaming.chunk.Chunk;

@Data
@NoArgsConstructor
public class StreamFinishedEvent {
    private Chunk chunkResult;
    private String finishReason;

    public StreamFinishedEvent(Chunk chunkResult, String finishReason) {
        this.chunkResult = chunkResult;
        this.finishReason = finishReason;
    }
}
