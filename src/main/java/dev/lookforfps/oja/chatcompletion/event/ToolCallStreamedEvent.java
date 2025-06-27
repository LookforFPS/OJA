package dev.lookforfps.oja.chatcompletion.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import dev.lookforfps.oja.chatcompletion.model.natives.tools.ToolCall;
import dev.lookforfps.oja.chatcompletion.model.streaming.chunk.Chunk;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ToolCallStreamedEvent extends ChunkStreamedEvent {
    private List<ToolCall> toolCalls;

    public ToolCallStreamedEvent(Chunk streamedChunk, Chunk chunkResult, List<ToolCall> toolCalls) {
        super(streamedChunk, chunkResult);
        this.toolCalls = toolCalls;
    }
}
