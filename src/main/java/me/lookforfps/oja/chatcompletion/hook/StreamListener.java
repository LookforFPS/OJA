package me.lookforfps.oja.chatcompletion.hook;

import me.lookforfps.oja.chatcompletion.event.ChunkStreamedEvent;

public interface StreamListener {

    void onChunkStreamed(ChunkStreamedEvent event);

    void onStreamStopped();
}
