package me.lookforfps.oja.chatcompletion;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.lookforfps.oja.chatcompletion.config.ChatCompletionConfiguration;
import me.lookforfps.oja.chatcompletion.request.ChatCompletionRequestDto;
import me.lookforfps.oja.chatcompletion.content.ContentList;
import me.lookforfps.oja.chatcompletion.response.ChatCompletionResponse;
import me.lookforfps.oja.chatcompletion.response.ChatCompletionResponseDto;
import me.lookforfps.oja.chatcompletion.entity.Message;
import me.lookforfps.oja.chatcompletion.streaming.Chunk;
import me.lookforfps.oja.chatcompletion.streaming.Stream;
import me.lookforfps.oja.chatcompletion.streaming.StreamListener;
import me.lookforfps.oja.chatcompletion.mapping.MappingService;
import me.lookforfps.oja.chatcompletion.streaming.event.ChunkStreamedEvent;
import me.lookforfps.oja.chatcompletion.streaming.event.StreamStoppedEvent;

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
    private final MappingService mappingService;

    public ChatCompletion(ChatCompletionConfiguration configuration) {
        this.config = configuration;
        this.mappingService = new MappingService();
    }

    public ChatCompletionResponse sendRequest() throws IOException {
        config.setStream(false);

        ChatCompletionRequestDto request = buildRequest();

        String url = "https://api.openai.com/v1/chat/completions";
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer "+config.getApiToken());

        con.setDoOutput(true);
        con.getOutputStream().write(mappingService.requestDtoToBytes(request));

        log.debug("request: "+mappingService.requestDtoToString(request));

        String output = new BufferedReader(new InputStreamReader(con.getInputStream())).lines()
                .reduce((a, b) -> a + b).get();

        log.debug("response: "+output);

        ChatCompletionResponseDto response = mappingService.bytesToResponseDto(output.getBytes(), ChatCompletionResponseDto.class);

        log.debug("processedResponse: "+mappingService.responseDtoToString(response));

        if(config.getAutoAddAIResponseToContext()) {
            addMessage(response.getChoices().get(0).getMessage());
        }

        return buildResponse(response);
    }

    public Stream sendStreamRequest() throws IOException {
        return sendStreamRequest(null);
    }

    public Stream sendStreamRequest(StreamListener listener) {
        config.setStream(true);

        Stream stream = new Stream();
        if (listener != null) {
            stream.addStreamListener(listener);
        }

        Thread streamThread = new Thread(() -> {
            try {
                ChatCompletionRequestDto request = buildRequest();

                String url = "https://api.openai.com/v1/chat/completions";
                HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Authorization", "Bearer " + config.getApiToken());

                con.setDoOutput(true);
                con.getOutputStream().write(mappingService.requestDtoToBytes(request));

                log.info("request: " + mappingService.requestDtoToString(request));

                Scanner scanner = new Scanner(con.getInputStream());

                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    line = line.replace("data: ", "");
                    log.info("chunk: " + line);

                    if(line.equalsIgnoreCase("[DONE]")) {
                        StreamStoppedEvent event = new StreamStoppedEvent("done"); // TODO reason anpassen
                        stream.emitStreamStopped(event);

                        log.info("stream stopped");
                        break;
                    } else if(line.equalsIgnoreCase("")) {
                        log.info("empty chunk skipped");
                    } else {
                        Chunk chunk = mappingService.bytesToChunk(line.getBytes());

                        ChunkStreamedEvent chunkStreamedEvent = new ChunkStreamedEvent(chunk);
                        stream.emitChunkStreamed(chunkStreamedEvent);
                        log.info("chunk shared");
                    }
                }
                scanner.close();
            } catch(IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        streamThread.setName("ChatCompletion-Stream");
        streamThread.start();


        return stream;
    }

    private ChatCompletionRequestDto buildRequest() {
        ChatCompletionRequestDto request = new ChatCompletionRequestDto();

        request.setModel(config.getModel().getIdentifier());
        request.setMessages(messages);

        request.setFrequency_penalty(config.getFrequencyPenalty());
        request.setLogit_bias(config.getLogitBias());
        request.setLogprobs(config.getLogprobs());
        request.setTop_logprobs(config.getTopLogprobs());
        request.setMax_tokens(config.getMaxTokens());
        request.setN(config.getChoices());
        request.setPresence_penalty(config.getPresencePenalty());
        request.setSeed(config.getSeed());
        request.setService_tier(config.getServiceTier());
        request.setStop(config.getStop());
        request.setStream(config.getStream());
        request.setStream_options(config.getStreamOptions());
        request.setTemperature(config.getTemperature());
        request.setTop_p(config.getTopP());
        request.setTools(config.getTools());
        request.setTool_choice(config.getToolChoice());
        request.setParallel_tool_calls(config.getParallelToolCalls());
        request.setUser(config.getUser());

        return request;
    }

    private ChatCompletionResponse buildResponse(ChatCompletionResponseDto responseDto) {
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

    public ChatCompletion addMessage(Message message) {
        messages.add(message);
        return this;
    }
    public ChatCompletion addImageMessage(String role, String imageUrl, String additionText) {
        addMessage(new Message(role, ContentList.addImageWithTextContent(imageUrl, additionText)));
        return this;
    }
    public ChatCompletion addImageMessage(String role, String imageUrl) {
        addMessage(new Message(role, ContentList.addImageContent(imageUrl)));
        return this;
    }
    public ChatCompletion addImageMessage(String imageUrl) {
        addImageMessage("user", imageUrl);
        return this;
    }
    public ChatCompletion addTextMessage(String role, String text) {
        addMessage(new Message(role, ContentList.addTextContent(text)));
        return this;
    }
    public ChatCompletion addTextMessage(String text) {
        addTextMessage("user", text);
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
