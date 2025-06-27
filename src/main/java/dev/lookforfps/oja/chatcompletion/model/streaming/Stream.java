package dev.lookforfps.oja.chatcompletion.model.streaming;

import dev.lookforfps.oja.chatcompletion.hook.StreamContainer;
import dev.lookforfps.oja.chatcompletion.hook.StreamListener;
import dev.lookforfps.oja.chatcompletion.model.streaming.chunk.Chunk;

public class Stream {

    private final StreamContainer streamContainer;

    public Stream(StreamContainer streamContainer) {
        this.streamContainer = streamContainer;
    }

    public void addStreamListener(StreamListener listener) {
        streamContainer.addStreamListener(listener);
    }
    public void removeStreamListener(StreamListener listener) {
        streamContainer.removeStreamListener(listener);
    }

    public Chunk getChunkResult() {
        return streamContainer.getChunkResult();
    }
}
