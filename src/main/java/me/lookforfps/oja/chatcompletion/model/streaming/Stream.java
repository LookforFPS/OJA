package me.lookforfps.oja.chatcompletion.model.streaming;

import lombok.Getter;
import me.lookforfps.oja.chatcompletion.hook.StreamListener;
import me.lookforfps.oja.chatcompletion.event.ChunkStreamedEvent;
import me.lookforfps.oja.chatcompletion.model.natives.tools.ToolCall;
import me.lookforfps.oja.chatcompletion.model.streaming.choice.Choice;

import java.util.ArrayList;
import java.util.List;

public class Stream {

    private List<StreamListener> listeners = new ArrayList<StreamListener>();
    @Getter
    private boolean streamStopped = false;
    @Getter
    private ArrayList<Choice> choices = new ArrayList<>();

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

    public void emitStreamStopped() {
        streamStopped = true;
        for (StreamListener listener : listeners) {
            listener.onStreamStopped();
        }
    }

    public void updateChoices(List<Choice> streamedChoices) {
        for (Choice streamedChoice : streamedChoices) {
            if(streamedChoice.getIndex() >= choices.size()) {
                choices.add(streamedChoice);
            } else {
                Choice savedChoice = choices.get(streamedChoice.getIndex());
                if(streamedChoice.getDelta().getTool_calls() != null && savedChoice.getDelta().getTool_calls() != null) {
                    List<ToolCall> updatedToolCalls = updateToolCalls(streamedChoice.getDelta().getTool_calls(), savedChoice.getDelta().getTool_calls());
                    savedChoice.getDelta().setTool_calls(updatedToolCalls);
                }
                if(streamedChoice.getDelta().getContent() != null && savedChoice.getDelta().getContent() != null) {
                    String oldContent = savedChoice.getDelta().getContent();
                    String newContent = streamedChoice.getDelta().getContent();
                    savedChoice.getDelta().setContent(oldContent + newContent);
                }
                choices.set(savedChoice.getIndex(), savedChoice);
            }
        }
    }

    private List<ToolCall> updateToolCalls(List<ToolCall> streamedToolCalls, List<ToolCall> savedToolCalls) {
        for(ToolCall streamedToolCall : streamedToolCalls) {
            ToolCall savedToolCall = savedToolCalls.get(streamedToolCall.getIndex());
            if(streamedToolCall.getFunction().getArguments() != null) {
                String oldArguments = savedToolCall.getFunction().getArguments();
                String newArguments = streamedToolCall.getFunction().getArguments();
                savedToolCall.getFunction().setArguments(oldArguments + newArguments);
                savedToolCalls.set(savedToolCall.getIndex(), savedToolCall);
            }
        }
        return savedToolCalls;
    }
}
