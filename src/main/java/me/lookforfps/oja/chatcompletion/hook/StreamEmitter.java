package me.lookforfps.oja.chatcompletion.hook;

import lombok.extern.slf4j.Slf4j;
import me.lookforfps.oja.chatcompletion.event.*;

import java.util.List;

@Slf4j
public class StreamEmitter {

    public static void emitChunkStreamed(ChunkStreamedEvent event, List<StreamListener> listeners) {
        for (StreamListener listener : listeners) {
            listener.onChunkStreamed(event);
        }
        log.debug("Emitted chunk streamed event to "+listeners.size()+" listeners");
    }

    public static void emitContentStreamed(ContentStreamedEvent event, List<StreamListener> listeners) {
        for (StreamListener listener : listeners) {
            listener.onContentStreamed(event);
        }
        log.debug("Emitted content streamed event to "+listeners.size()+" listeners");
    }

    public static void emitUsageStreamed(UsageStreamedEvent event, List<StreamListener> listeners) {
        for (StreamListener listener : listeners) {
            listener.onUsageStreamed(event);
        }
        log.debug("Emitted usage streamed event to "+listeners.size()+" listeners");
    }

    public static void emitToolCallStreamed(ToolCallStreamedEvent event, List<StreamListener> listeners) {
        for (StreamListener listener : listeners) {
            listener.onToolCallStreamed(event);
        }
        log.debug("Emitted toolcall streamed event to "+listeners.size()+" listeners");
    }

    public static void emitStreamFinished(StreamFinishedEvent event, List<StreamListener> listeners) {
        for (StreamListener listener : listeners) {
            listener.onStreamFinished(event);
        }
        log.debug("Emitted stream finished event to "+listeners.size()+" listeners");
    }

    public static void emitStreamStopped(StreamStoppedEvent event, List<StreamListener> listeners) {
        for (StreamListener listener : listeners) {
            listener.onStreamStopped(event);
        }
        log.debug("Emitted stream stopped event to "+listeners.size()+" listeners");
    }

    public static void emitStreamFailed(Exception exception, List<StreamListener> listeners) {
        for (StreamListener listener : listeners) {
            listener.onStreamFailed(exception);
        }
        log.debug("Emitted stream failed event to "+listeners.size()+" listeners");
    }

}
