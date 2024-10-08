package me.lookforfps.oja.chatcompletion;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.lookforfps.oja.chatcompletion.event.*;
import me.lookforfps.oja.chatcompletion.hook.StreamEmitter;
import me.lookforfps.oja.chatcompletion.config.ChatCompletionConfiguration;
import me.lookforfps.oja.chatcompletion.model.natives.message.AssistantMessage;
import me.lookforfps.oja.chatcompletion.model.natives.request.ChatCompletionRequestDto;
import me.lookforfps.oja.chatcompletion.model.natives.request.ResponseFormat;
import me.lookforfps.oja.chatcompletion.model.natives.response.ChatCompletionResponse;
import me.lookforfps.oja.chatcompletion.model.natives.response.ChatCompletionResponseDto;
import me.lookforfps.oja.chatcompletion.model.natives.message.Message;
import me.lookforfps.oja.chatcompletion.hook.StreamContainer;
import me.lookforfps.oja.chatcompletion.model.streaming.StreamOptions;
import me.lookforfps.oja.chatcompletion.model.streaming.choice.Choice;
import me.lookforfps.oja.chatcompletion.model.streaming.chunk.Chunk;
import me.lookforfps.oja.chatcompletion.model.streaming.Stream;
import me.lookforfps.oja.chatcompletion.hook.StreamListener;
import me.lookforfps.oja.chatcompletion.mapping.MappingService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Slf4j
public class ChatCompletionService {

    @Getter
    @Setter
    private ChatCompletionConfiguration config;
    @Getter
    @Setter
    private List<Message> context = new ArrayList<>();
    private final MappingService mappingService;

    private ChatCompletionService(ChatCompletionConfiguration configuration) {
        this.config = configuration;
        this.mappingService = new MappingService();
    }

    public static ChatCompletionService build(String apiToken, String modelIdentifier, ChatCompletionConfiguration configuration) {
        if(configuration==null) {
            configuration = new ChatCompletionConfiguration();
        }
        configuration.setApiToken(apiToken);
        configuration.setModel(modelIdentifier);
        if(configuration.getApiToken()!=null && configuration.getModel()!=null) {
            return new ChatCompletionService(configuration);
        } else {
            log.error("The API Token or/and Model is not set!");
            return null;
        }
    }
    public static ChatCompletionService build(ChatCompletionConfiguration configuration) {
        return build(configuration.getApiToken(), configuration.getModel(), configuration);
    }
    public static ChatCompletionService build(String apiToken, String modelIdentifier) {
        return build(apiToken, modelIdentifier, null);
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

        if(config.getAddAIResponseToContext()) {
            AssistantMessage message = response.getChoices().get(config.getAiResponseChoiceIndex()).getMessage().asAssistantMessage();
            if(message != null) {
                addMessage(new AssistantMessage(message.getContent(), message.getTool_calls()));
            }
        }

        return response;
    }

    public Stream sendStreamRequest() throws IOException {
        return sendStreamRequest(null);
    }

    public Stream sendStreamRequest(StreamListener listener) throws IOException {
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
                    Choice choice = streamContainer.getChunkResult().getChoices().get(config.getAiResponseChoiceIndex());
                    if(choice != null && choice.getDelta() != null) {
                        addMessage(new AssistantMessage(choice.getDelta().getContent(), choice.getDelta().getTool_calls()));
                    }
                }

                StreamStoppedEvent streamStoppedEvent = new StreamStoppedEvent(streamContainer.getChunkResult());
                StreamEmitter.emitStreamStopped(streamStoppedEvent, streamContainer.getListeners());
                log.debug("stream stopped");
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

        requestDto.setModel(config.getModel());
        requestDto.setMessages(context);

        requestDto.setFrequency_penalty(config.getFrequencyPenalty());
        requestDto.setLogit_bias(config.getLogitBias());
        requestDto.setLogprobs(config.getLogprobs());
        requestDto.setTop_logprobs(config.getTopLogprobs());
        requestDto.setMax_tokens(config.getMaxTokens());
        requestDto.setN(config.getChoices());
        requestDto.setPresence_penalty(config.getPresencePenalty());
        requestDto.setResponse_format(config.getResponseFormat());
        requestDto.setSeed(config.getSeed());
        if(config.getServiceTier() != null) {
            requestDto.setService_tier(config.getServiceTier().getIdentifier());
        }
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

        log.debug("requestDto: " + mappingService.requestDtoToString(requestDto));

        return mappingService.requestDtoToBytes(requestDto);
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

        ChatCompletionResponseDto responseDto = mappingService.bytesToResponseDto(rawResponse.getBytes());
        log.debug("processedResponseDto: "+ mappingService.responseDtoToString(responseDto));

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
            StreamFinishedEvent streamFinishedEvent = new StreamFinishedEvent(streamContainer.getChunkResult(), streamContainer.getChunkResult().getChoices().get(0).getFinish_reason());
            StreamEmitter.emitStreamFinished(streamFinishedEvent, streamContainer.getListeners());
            log.debug("stream finished");
            return true;
        } else if(output.startsWith("{")) {
            Chunk chunk = mappingService.bytesToChunk(output.getBytes());
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
        } else if(chunk.getChoices().get(0).getDelta().getTool_calls() != null) {
            ToolCallStreamedEvent toolCallStreamedEvent = new ToolCallStreamedEvent(chunk, streamContainer.getChunkResult(), chunk.getChoices().get(0).getDelta().getTool_calls());
            StreamEmitter.emitToolCallStreamed(toolCallStreamedEvent, streamContainer.getListeners());
        } else if(chunk.getChoices().get(0).getDelta().getContent() != null) {
            ContentStreamedEvent contentStreamedEvent = new ContentStreamedEvent(chunk, streamContainer.getChunkResult(), chunk.getChoices());
            StreamEmitter.emitContentStreamed(contentStreamedEvent, streamContainer.getListeners());
        }
    }

    public ChatCompletionService addMessage(Message message) {
        context.add(message);
        return this;
    }
    public ChatCompletionService removeMessage(int index) {
        context.remove(index);
        return this;
    }
    public ChatCompletionService removeMessage(Message message) {
        context.remove(message);
        return this;
    }
    public ChatCompletionService removeAllMessages() {
        context.clear();
        return this;
    }
}
