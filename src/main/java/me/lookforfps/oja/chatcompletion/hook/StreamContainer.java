package me.lookforfps.oja.chatcompletion.hook;

import lombok.Data;
import me.lookforfps.oja.chatcompletion.model.natives.response.Usage;
import me.lookforfps.oja.chatcompletion.model.natives.tools.ToolCall;
import me.lookforfps.oja.chatcompletion.model.streaming.choice.Choice;
import me.lookforfps.oja.chatcompletion.model.streaming.chunk.Chunk;

import java.util.ArrayList;
import java.util.List;

@Data
public class StreamContainer {

    private List<StreamListener> listeners = new ArrayList<StreamListener>();
    private Chunk chunkResult;

    public void addStreamListener(StreamListener listener) {
        listeners.add(listener);
    }
    public void removeStreamListener(StreamListener listener) {
        listeners.remove(listener);
    }

    public void updateChunkResult(Chunk streamedChunk) {
        if(chunkResult == null) {
            chunkResult = streamedChunk;
        } else {
            for (Choice streamedChoice : streamedChunk.getChoices()) {
                if(streamedChoice.getIndex() >= chunkResult.getChoices().size()) {
                    chunkResult.getChoices().add(streamedChoice);
                } else {
                    Choice savedChoice = chunkResult.getChoices().get(streamedChoice.getIndex());
                    if(streamedChoice.getDelta().getTool_calls() != null) {
                        updateToolCalls(streamedChoice.getDelta().getTool_calls(), savedChoice.getDelta().getTool_calls());
                    }
                    if(streamedChoice.getDelta().getContent() != null) {
                        updateContent(streamedChoice, savedChoice);
                    }
                    if(streamedChoice.getFinish_reason() != null) {
                        updateFinishReason(streamedChoice, savedChoice);
                    }
                    chunkResult.getChoices().set(savedChoice.getIndex(), savedChoice);
                }
            }
            if(streamedChunk.getUsage() != null) {
                updateUsage(streamedChunk, chunkResult);
            }
        }
    }

    private void updateContent(Choice streamedChoice, Choice savedChoice) {
        if(savedChoice.getDelta().getContent() == null) {
            savedChoice.getDelta().setContent("");
        }
        String oldContent = savedChoice.getDelta().getContent();
        String newContent = streamedChoice.getDelta().getContent();
        savedChoice.getDelta().setContent(oldContent + newContent);
    }

    private void updateUsage(Chunk streamedChunk, Chunk savedChunk) {
        if(savedChunk.getUsage() == null) {
            savedChunk.setUsage(new Usage());
        }
        savedChunk.getUsage().setCompletion_tokens(streamedChunk.getUsage().getCompletion_tokens());
        savedChunk.getUsage().setPrompt_tokens(streamedChunk.getUsage().getPrompt_tokens());
        savedChunk.getUsage().setTotal_tokens(streamedChunk.getUsage().getTotal_tokens());
    }

    private void updateToolCalls(List<ToolCall> streamedToolCalls, List<ToolCall> savedToolCalls) {
        if(savedToolCalls == null) {
            savedToolCalls = new ArrayList<>();
        }
        for(ToolCall streamedToolCall : streamedToolCalls) {
            ToolCall savedToolCall = savedToolCalls.get(streamedToolCall.getIndex());
            if(streamedToolCall.getFunction().getArguments() != null) {
                String oldArguments = savedToolCall.getFunction().getArguments();
                String newArguments = streamedToolCall.getFunction().getArguments();
                savedToolCall.getFunction().setArguments(oldArguments + newArguments);
                savedToolCalls.set(savedToolCall.getIndex(), savedToolCall);
            }
        }
    }

    private void updateFinishReason(Choice streamedChoice, Choice savedChoice) {
        savedChoice.setFinish_reason(streamedChoice.getFinish_reason());
    }

}