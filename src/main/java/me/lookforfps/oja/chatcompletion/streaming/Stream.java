package me.lookforfps.oja.chatcompletion.streaming;

import lombok.Getter;
import me.lookforfps.oja.chatcompletion.streaming.event.ChunkStreamedEvent;
import me.lookforfps.oja.chatcompletion.streaming.event.StreamStoppedEvent;

import java.util.ArrayList;
import java.util.List;

public class Stream {

    private List<StreamListener> listeners = new ArrayList<StreamListener>();
    @Getter
    private boolean streamStopped = false;

    public void addStreamListener(StreamListener listener) {
        listeners.add(listener);
    }
    public void removeStreamListener(StreamListener listener) {
        listeners.remove(listener);
    }


    public void emitChunkStreamed(ChunkStreamedEvent event) {
        for (StreamListener listener : listeners) {
            listener.onChunkStreamed(event);
        }
    }

    public void emitStreamStopped(StreamStoppedEvent event) {
        streamStopped = true;
        for (StreamListener listener : listeners) {
            listener.onStreamStopped(event);
        }
    }
}
