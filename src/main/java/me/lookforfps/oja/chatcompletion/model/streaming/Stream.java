package me.lookforfps.oja.chatcompletion.model.streaming;

import lombok.Getter;
import me.lookforfps.oja.chatcompletion.hook.StreamListener;
import me.lookforfps.oja.chatcompletion.event.ChunkStreamedEvent;
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
                Choice choice = choices.get(streamedChoice.getIndex());
                String oldContent = choice.getDelta().getContent();
                String newContent = streamedChoice.getDelta().getContent();
                choice.getDelta().setContent(oldContent + newContent);
                choices.set(choice.getIndex(), choice);
            }
        }

    }
}
