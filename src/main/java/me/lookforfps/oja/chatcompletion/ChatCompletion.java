package me.lookforfps.oja.chatcompletion;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.lookforfps.oja.chatcompletion.event.*;
import me.lookforfps.oja.chatcompletion.hook.StreamEmitter;
import me.lookforfps.oja.chatcompletion.model.natives.config.ChatCompletionConfiguration;
import me.lookforfps.oja.chatcompletion.model.natives.message.MessageRole;
import me.lookforfps.oja.chatcompletion.model.natives.request.ChatCompletionRequestDto;
import me.lookforfps.oja.chatcompletion.model.natives.content.ContentList;
import me.lookforfps.oja.chatcompletion.model.natives.request.ResponseFormat;
import me.lookforfps.oja.chatcompletion.model.natives.response.ChatCompletionResponse;
import me.lookforfps.oja.chatcompletion.model.natives.response.ChatCompletionResponseDto;
import me.lookforfps.oja.chatcompletion.model.natives.message.Message;
import me.lookforfps.oja.chatcompletion.hook.StreamContainer;
import me.lookforfps.oja.chatcompletion.model.streaming.StreamOptions;
import me.lookforfps.oja.chatcompletion.model.streaming.chunk.Chunk;
import me.lookforfps.oja.chatcompletion.model.streaming.Stream;
import me.lookforfps.oja.chatcompletion.hook.StreamListener;
import me.lookforfps.oja.chatcompletion.mapping.Mapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Slf4j
public class ChatCompletion {

    @Getter
    @Setter
    private ChatCompletionConfiguration config;
    @Getter
    @Setter
    private List<Message> messages = new ArrayList<>();
    private final Mapper mapper;

    public ChatCompletion(ChatCompletionConfiguration configuration) {
        this.config = configuration;
        this.mapper = new Mapper();
    }

    public ChatCompletionResponse sendRequest() throws IOException, URISyntaxException {
        config.setStream(false);

        byte[] request = buildRequest();
        HttpURLConnection con = buildConnection();

        con.getOutputStream().write(request);

        String output = new BufferedReader(new InputStreamReader(con.getInputStream())).lines()
                .reduce((a, b) -> a + b).get();

        con.disconnect();

        ChatCompletionResponse response = buildResponse(output);

        if(config.getAddAIResponseToContext()) {
            Message aiMessage = new Message(
                    MessageRole.SYSTEM,
                    ContentList.addTextContent(response.getChoices().get(config.getAiResponseChoiceIndex()).getFirstTextContent().getText()));
            addMessage(aiMessage);
        }

        return response;
    }

    public Stream sendStreamRequest() throws IOException, URISyntaxException {
        return sendStreamRequest(null);
    }

    public Stream sendStreamRequest(StreamListener listener) throws IOException, URISyntaxException {
        config.setStream(true);

        StreamContainer streamContainer = new StreamContainer();
        Stream stream = new Stream(streamContainer);
        if (listener != null) {
            stream.addStreamListener(listener);
        }

        Thread streamThread = new Thread(() -> {
            try {
                byte[] request = buildRequest();
                HttpURLConnection con = buildConnection();

                con.getOutputStream().write(request);

                Scanner scanner = new Scanner(con.getInputStream());
                while (scanner.hasNextLine()) {
                    if(classifyChunk(streamContainer, scanner.nextLine())) {
                        break;
                    }
                }
                scanner.close();
                con.disconnect();

                if(config.getAddAIResponseToContext()) {
                    Message aiMessage = new Message(
                            MessageRole.SYSTEM,
                            ContentList.addTextContent(streamContainer.getChunkResult().getChoices().get(config.getAiResponseChoiceIndex()).getDelta().getContent()));
                    addMessage(aiMessage);
                }
            } catch(IOException | URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
        });
        streamThread.setName("ChatCompletion-Stream");
        streamThread.start();


        return stream;
    }

    private byte[] buildRequest() throws IOException {
        ChatCompletionRequestDto requestDto = new ChatCompletionRequestDto();

        requestDto.setModel(config.getAIModel().getIdentifier());
        requestDto.setMessages(messages);

        requestDto.setFrequency_penalty(config.getFrequencyPenalty());
        requestDto.setLogit_bias(config.getLogitBias());
        requestDto.setLogprobs(config.getLogprobs());
        requestDto.setTop_logprobs(config.getTopLogprobs());
        requestDto.setMax_tokens(config.getMaxTokens());
        requestDto.setN(config.getChoices());
        requestDto.setPresence_penalty(config.getPresencePenalty());
        if(config.getResponseType() != null) {
            requestDto.setResponse_format(new ResponseFormat(config.getResponseType().getIdentifier()));
        }
        requestDto.setSeed(config.getSeed());
        requestDto.setService_tier(config.getServiceTier());
        requestDto.setStop(config.getStop());
        requestDto.setStream(config.getStream());
        if(config.getIncludeUsageToStream() != null) {
            requestDto.setStream_options(new StreamOptions(config.getIncludeUsageToStream()));
        }
        requestDto.setTemperature(config.getTemperature());
        requestDto.setTop_p(config.getTopP());
        requestDto.setTools(config.getTools());
        requestDto.setTool_choice(config.getToolChoice());
        requestDto.setParallel_tool_calls(config.getParallelToolCalls());
        requestDto.setUser(config.getUser());

        log.debug("requestDto: " + mapper.requestDtoToString(requestDto));

        return mapper.requestDtoToBytes(requestDto);
    }

    private HttpURLConnection buildConnection() throws IOException, URISyntaxException {
        HttpURLConnection con = (HttpURLConnection) new URI(config.getApiUrl()).toURL().openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer " + config.getApiToken());

        con.setDoOutput(true);
        return con;
    }

    private ChatCompletionResponse buildResponse(String rawResponse) throws IOException {
        log.debug("rawResponse: "+rawResponse);

        ChatCompletionResponseDto responseDto = mapper.bytesToResponseDto(rawResponse.getBytes(), ChatCompletionResponseDto.class);
        log.debug("processedResponseDto: "+ mapper.responseDtoToString(responseDto));

        ChatCompletionResponse response = new ChatCompletionResponse();

        response.setId(responseDto.getId());
        response.setObjectType(responseDto.getObject());
        response.setCreated(responseDto.getCreated());
        response.setUsedModel(responseDto.getModel());
        response.setSystemFingerprint(responseDto.getSystem_fingerprint());
        response.setUsage(responseDto.getUsage());
        response.setChoices(responseDto.getChoices());
        response.setServiceTier(responseDto.getService_tier());

        return response;
    }

    private boolean classifyChunk(StreamContainer streamContainer, String rawChunk) throws IOException {
        String output = rawChunk.replace("data: ", "");
        log.debug("chunkResponse: "+output);

        if(output.equalsIgnoreCase("[DONE]")) {
            StreamStoppedEvent streamStoppedEvent = new StreamStoppedEvent(streamContainer.getChunkResult());
            StreamEmitter.emitStreamStopped(streamStoppedEvent, streamContainer.getListeners());
            log.debug("stream stopped");
            return true;
        } else if(output.startsWith("{")) {
            Chunk chunk = mapper.bytesToChunk(output.getBytes());
            classifyChunkContent(streamContainer, chunk);
        } else {
            log.debug("empty chunk skipped");
        }
        return false;
    }

    private void classifyChunkContent(StreamContainer streamContainer, Chunk chunk) {
        streamContainer.updateChunkResult(chunk);
        ChunkStreamedEvent chunkStreamedEvent = new ChunkStreamedEvent(chunk, streamContainer.getChunkResult());
        StreamEmitter.emitChunkStreamed(chunkStreamedEvent, streamContainer.getListeners());

        if(chunk.getUsage() != null) {
            UsageStreamedEvent usageStreamedEvent = new UsageStreamedEvent(chunk, streamContainer.getChunkResult(), chunk.getUsage());
            StreamEmitter.emitUsageStreamed(usageStreamedEvent, streamContainer.getListeners());
        } else if(chunk.getChoices().getFirst().getFinish_reason() != null) {
            StreamFinishedEvent streamFinishedEvent = new StreamFinishedEvent(chunk, streamContainer.getChunkResult(), chunk.getChoices().getFirst().getFinish_reason());
            StreamEmitter.emitStreamFinished(streamFinishedEvent, streamContainer.getListeners());
        } else if(chunk.getChoices().getFirst().getDelta().getTool_calls() != null) {
            ToolCallStreamedEvent toolCallStreamedEvent = new ToolCallStreamedEvent(chunk, streamContainer.getChunkResult(), chunk.getChoices().getFirst().getDelta().getTool_calls());
            StreamEmitter.emitToolCallStreamed(toolCallStreamedEvent, streamContainer.getListeners());
        } else {
            ContentStreamedEvent contentStreamedEvent = new ContentStreamedEvent(chunk, streamContainer.getChunkResult(), chunk.getChoices());
            StreamEmitter.emitContentStreamed(contentStreamedEvent, streamContainer.getListeners());
        }
    }

    public ChatCompletion addMessage(Message message) {
        messages.add(message);
        return this;
    }
    public ChatCompletion addImageMessage(MessageRole role, String imageUrl, String additionText) {
        addMessage(new Message(role, ContentList.addImageWithTextContent(imageUrl, additionText)));
        return this;
    }
    public ChatCompletion addImageMessage(MessageRole role, String imageUrl) {
        addMessage(new Message(role, ContentList.addImageContent(imageUrl)));
        return this;
    }
    public ChatCompletion addImageMessage(String imageUrl) {
        addImageMessage(MessageRole.USER, imageUrl);
        return this;
    }
    public ChatCompletion addTextMessage(MessageRole role, String text) {
        addMessage(new Message(role, ContentList.addTextContent(text)));
        return this;
    }
    public ChatCompletion addTextMessage(String text) {
        addTextMessage(MessageRole.USER, text);
        return this;
    }
    public ChatCompletion removeMessage(int index) {
        messages.remove(index);
        return this;
    }
    public ChatCompletion removeMessage(Message message) {
        messages.remove(message);
        return this;
    }
}
