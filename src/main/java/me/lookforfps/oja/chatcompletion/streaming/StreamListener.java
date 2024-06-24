package me.lookforfps.oja.chatcompletion.streaming;

import me.lookforfps.oja.chatcompletion.streaming.event.ChunkStreamedEvent;
import me.lookforfps.oja.chatcompletion.streaming.event.StreamStoppedEvent;

public interface StreamListener {

    void onChunkStreamed(ChunkStreamedEvent event);

    void onStreamStopped(StreamStoppedEvent event);
}
