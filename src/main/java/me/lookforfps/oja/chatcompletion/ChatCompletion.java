package me.lookforfps.oja.chatcompletion;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.lookforfps.oja.chatcompletion.model.natives.config.ChatCompletionConfiguration;
import me.lookforfps.oja.chatcompletion.model.natives.message.MessageRole;
import me.lookforfps.oja.chatcompletion.model.natives.request.ChatCompletionRequestDto;
import me.lookforfps.oja.chatcompletion.model.natives.content.ContentList;
import me.lookforfps.oja.chatcompletion.model.natives.response.ChatCompletionResponse;
import me.lookforfps.oja.chatcompletion.model.natives.response.ChatCompletionResponseDto;
import me.lookforfps.oja.chatcompletion.model.natives.message.Message;
import me.lookforfps.oja.chatcompletion.model.streaming.chunk.Chunk;
import me.lookforfps.oja.chatcompletion.model.streaming.Stream;
import me.lookforfps.oja.chatcompletion.hook.StreamListener;
import me.lookforfps.oja.chatcompletion.mapping.Mapper;
import me.lookforfps.oja.chatcompletion.event.ChunkStreamedEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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

    public ChatCompletionResponse sendRequest() throws IOException {
        config.setStream(false);

        byte[] request = buildRequest();
        HttpURLConnection con = buildConnection();

        con.getOutputStream().write(request);

        String output = new BufferedReader(new InputStreamReader(con.getInputStream())).lines()
                .reduce((a, b) -> a + b).get();

        con.disconnect();

        ChatCompletionResponse response = buildResponse(output);

        if(config.getAutoAddAIResponseToContext()) {
            addMessage(response.getChoices().get(config.getAutoAddAIResponseChoiceIndex()).getMessage());
        }

        return response;
    }

    public Stream sendStreamRequest() throws IOException {
        return sendStreamRequest(null);
    }

    public Stream sendStreamRequest(StreamListener listener) throws IOException {
        config.setStream(true);

        Stream stream = new Stream();
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
                    if(classifyChunk(stream, scanner.nextLine())) {
                        break;
                    }
                }
                scanner.close();
                con.disconnect();

                if(config.getAutoAddAIResponseToContext()) {
                    log.warn("Automatically adding AI responses to context is currently not available for streaming!");
                }
            } catch(IOException ex) {
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
        requestDto.setSeed(config.getSeed());
        requestDto.setService_tier(config.getServiceTier());
        requestDto.setStop(config.getStop());
        requestDto.setStream(config.getStream());
        requestDto.setStream_options(config.getStreamOptions());
        requestDto.setTemperature(config.getTemperature());
        requestDto.setTop_p(config.getTopP());
        requestDto.setTools(config.getTools());
        requestDto.setTool_choice(config.getToolChoice());
        requestDto.setParallel_tool_calls(config.getParallelToolCalls());
        requestDto.setUser(config.getUser());

        log.debug("requestDto: " + mapper.requestDtoToString(requestDto));

        return mapper.requestDtoToBytes(requestDto);
    }

    private HttpURLConnection buildConnection() throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(config.getApiUrl()).openConnection();

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

    private boolean classifyChunk(Stream stream, String rawChunk) throws IOException {
        String output = rawChunk.replace("data: ", "");
        log.debug("chunkResponse: "+output);

        if(output.equalsIgnoreCase("[DONE]")) {
            stream.emitStreamStopped();
            log.debug("stream stopped");
            return true;
        } else if(output.equalsIgnoreCase("")) {
            log.debug("empty chunk skipped");
        } else if(rawChunk.startsWith("data:")) {
            Chunk chunk = mapper.bytesToChunk(output.getBytes());
            if(chunk.getChoices().get(0).getDelta().getContent() == null && chunk.getChoices().get(0).getDelta().getTool_calls() == null) {
                log.debug("empty chunk skipped");
            } else {
                stream.updateChoices(chunk.getChoices());
                ChunkStreamedEvent chunkStreamedEvent = new ChunkStreamedEvent(chunk);
                stream.emitChunkStreamed(chunkStreamedEvent);
                log.debug("chunk shared");
            }
        } else {
            log.error("Error during classifying chunk!");
        }
        return false;
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
