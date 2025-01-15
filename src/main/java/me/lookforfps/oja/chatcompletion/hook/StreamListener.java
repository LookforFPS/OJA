package me.lookforfps.oja.chatcompletion.hook;

import me.lookforfps.oja.chatcompletion.event.*;

public interface StreamListener {

    void onChunkStreamed(ChunkStreamedEvent event);

    void onContentStreamed(ContentStreamedEvent event);

    void onUsageStreamed(UsageStreamedEvent event);

    void onToolCallStreamed(ToolCallStreamedEvent event);

    void onStreamFinished(StreamFinishedEvent event);

    void onStreamStopped(StreamStoppedEvent event);

    void onStreamFailed(Exception exception);
}
