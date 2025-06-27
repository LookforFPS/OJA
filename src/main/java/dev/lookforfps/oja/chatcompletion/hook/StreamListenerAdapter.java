package dev.lookforfps.oja.chatcompletion.hook;

import dev.lookforfps.oja.chatcompletion.event.*;
import dev.lookforfps.oja.chatcompletion.event.*;

public class StreamListenerAdapter implements StreamListener {

    @Override
    public void onChunkStreamed(ChunkStreamedEvent event) {}

    @Override
    public void onContentStreamed(ContentStreamedEvent event) {}

    @Override
    public void onUsageStreamed(UsageStreamedEvent event) {}

    @Override
    public void onToolCallStreamed(ToolCallStreamedEvent event) {}

    @Override
    public void onStreamFinished(StreamFinishedEvent event) {}

    @Override
    public void onStreamStopped(StreamStoppedEvent event) {}

    @Override
    public void onStreamFailed(Exception exception) {}

}
