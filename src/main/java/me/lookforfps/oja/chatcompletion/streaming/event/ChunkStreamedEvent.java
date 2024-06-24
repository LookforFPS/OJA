package me.lookforfps.oja.chatcompletion.streaming.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.lookforfps.oja.chatcompletion.streaming.Chunk;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChunkStreamedEvent {
    private Chunk chunk;
}
