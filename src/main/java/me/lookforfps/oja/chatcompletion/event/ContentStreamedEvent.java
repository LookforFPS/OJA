package me.lookforfps.oja.chatcompletion.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import me.lookforfps.oja.chatcompletion.model.streaming.choice.Choice;
import me.lookforfps.oja.chatcompletion.model.streaming.chunk.Chunk;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ContentStreamedEvent extends ChunkStreamedEvent {
    private List<Choice> choices;

    public ContentStreamedEvent(Chunk streamedChunk, Chunk chunkResult, List<Choice> choices) {
        super(streamedChunk, chunkResult);
        this.choices = choices;
    }
}
