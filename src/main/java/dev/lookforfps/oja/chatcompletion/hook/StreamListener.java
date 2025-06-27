package dev.lookforfps.oja.chatcompletion.hook;

import dev.lookforfps.oja.chatcompletion.event.*;
import dev.lookforfps.oja.chatcompletion.event.*;

public interface StreamListener {

    void onChunkStreamed(ChunkStreamedEvent event);

    void onContentStreamed(ContentStreamedEvent event);

    void onUsageStreamed(UsageStreamedEvent event);

    void onToolCallStreamed(ToolCallStreamedEvent event);

    void onStreamFinished(StreamFinishedEvent event);

    void onStreamStopped(StreamStoppedEvent event);

    void onStreamFailed(Exception exception);
}
