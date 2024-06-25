package me.lookforfps.oja.chatcompletion.streaming;

import me.lookforfps.oja.chatcompletion.streaming.event.ChunkStreamedEvent;

public interface StreamListener {

    void onChunkStreamed(ChunkStreamedEvent event);

    void onStreamStopped();
}
