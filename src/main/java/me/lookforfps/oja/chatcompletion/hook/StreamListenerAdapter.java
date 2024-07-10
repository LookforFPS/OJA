package me.lookforfps.oja.chatcompletion.hook;

import me.lookforfps.oja.chatcompletion.event.*;

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

}
