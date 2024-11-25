package me.lookforfps.oja.chatcompletion;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.lookforfps.oja.aimodel.ChatCompletionModel;
import me.lookforfps.oja.chatcompletion.event.*;
import me.lookforfps.oja.chatcompletion.hook.StreamEmitter;
import me.lookforfps.oja.chatcompletion.config.ChatCompletionConfiguration;
import me.lookforfps.oja.chatcompletion.model.natives.message.AssistantMessage;
import me.lookforfps.oja.chatcompletion.model.natives.request.ChatCompletionRequestDto;
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
import me.lookforfps.oja.error.ErrorHandler;
import me.lookforfps.oja.error.exception.ApiErrorException;
import me.lookforfps.oja.error.exception.RequestErrorException;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ChatCompletionService {

    @Getter
    @Setter
    private ChatCompletionConfiguration config;
    @Getter
    @Setter
    private List<Message> context = new ArrayList<>();

    private final MappingService mappingService;
    private final ErrorHandler errorHandler;

    private ChatCompletionService(ChatCompletionConfiguration configuration) {
        this.config = configuration;
        this.mappingService = new MappingService();
        this.errorHandler = new ErrorHandler();
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
    public static ChatCompletionService build(String apiToken, ChatCompletionModel model, ChatCompletionConfiguration configuration) {
        return build(apiToken, model.getIdentifier(), configuration);
    }
    public static ChatCompletionService build(ChatCompletionConfiguration configuration) {
        return build(configuration.getApiToken(), configuration.getModel(), configuration);
    }
    public static ChatCompletionService build(String apiToken, ChatCompletionModel model) {
        return build(apiToken, model.getIdentifier(), null);
    }
    public static ChatCompletionService build(String apiToken, String modelIdentifier) {
        return build(apiToken, modelIdentifier, null);
    }


    public ChatCompletionResponse sendRequest() throws ApiErrorException, RequestErrorException {
        config.setStream(null);

        OkHttpClient client = new OkHttpClient();

        try {
            RequestBody requestBody = buildRequestBody();
            Request request = buildRequest(requestBody);

            Response response = client.newCall(request).execute();

            if(response.code() == 200) {
                ChatCompletionResponse ccResponse = buildResponseContent(response.body().string());

                if(config.getAddAIResponseToContext()) {
                    AssistantMessage message = ccResponse.getChoices().get(config.getAiResponseChoiceIndex()).getMessage().asAssistantMessage();
                    if(message != null) {
                        addMessage(new AssistantMessage(message.getContent(), message.getTool_calls()));
                    }
                }
                return ccResponse;
            } else {
                errorHandler.handleApiError(response);
                return null;
            }
        } catch (UnknownHostException ex) {
            log.error("DNS resolution failed: {}", ex.getMessage());
            errorHandler.handleRequestError(ex);
            return null;
        } catch (IOException ex) {
            log.error("I/O error during request: {}", ex.getMessage());
            errorHandler.handleRequestError(ex);
            return null;
        }
    }

    public Stream sendStreamRequest() {
        return sendStreamRequest(null);
    }
    public Stream sendStreamRequest(StreamListener listener) {
        config.setStream(true);

        StreamContainer streamContainer = new StreamContainer();
        Stream stream = new Stream(streamContainer);
        if (listener != null) {
            stream.addStreamListener(listener);
        }

        OkHttpClient client = new OkHttpClient();

        try {
            RequestBody requestBody = buildRequestBody();
            Request request = buildRequest(requestBody);

            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                    if(response.code() == 200) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().byteStream()));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            if(classifyChunk(streamContainer, line)) {
                                break;
                            }
                        }

                        if(config.getAddAIResponseToContext()) {
                            Choice choice = streamContainer.getChunkResult().getChoices().get(config.getAiResponseChoiceIndex());
                            if(choice != null && choice.getDelta() != null) {
                                addMessage(new AssistantMessage(choice.getDelta().getContent(), choice.getDelta().getTool_calls()));
                            }
                        }

                        StreamStoppedEvent streamStoppedEvent = new StreamStoppedEvent(streamContainer.getChunkResult());
                        StreamEmitter.emitStreamStopped(streamStoppedEvent, streamContainer.getListeners());
                        log.debug("Stream stopped");
                    } else {
                        errorHandler.handleApiError(response, streamContainer);
                    }
                }

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException ex) {
                    if(ex.getClass().equals(UnknownHostException.class)) {
                        log.error("DNS resolution failed: {}", ex.getMessage());
                        errorHandler.handleRequestError(ex, streamContainer);
                    } else if (ex.getClass().equals(IOException.class)) {
                        log.error("I/O error during request: {}", ex.getMessage());
                        errorHandler.handleRequestError(ex, streamContainer);
                    }
                }
            });

            return stream;
        } catch (IOException ex) {
            log.error("I/O error during request: {}", ex.getMessage());
            errorHandler.handleRequestError(ex, streamContainer);
            return null;
        }
    }

    private Request buildRequest(RequestBody requestBody) throws JsonProcessingException {
        return new Request.Builder()
                .url(config.getApiUrl())
                .addHeader("Authorization", "Bearer " + config.getApiToken())
                .post(requestBody)
                .build();
    }

    private RequestBody buildRequestBody() throws JsonProcessingException {
        ChatCompletionRequestDto requestDto = new ChatCompletionRequestDto();

        requestDto.setModel(config.getModel());
        requestDto.setMessages(context);

        requestDto.setStore(config.getStore());
        requestDto.setMetadata(config.getMetadata());
        requestDto.setFrequency_penalty(config.getFrequencyPenalty());
        requestDto.setLogit_bias(config.getLogitBias());
        requestDto.setLogprobs(config.getLogprobs());
        requestDto.setTop_logprobs(config.getTopLogprobs());
        requestDto.setMax_tokens(config.getMaxTokens());
        requestDto.setMax_completion_tokens(config.getMaxCompletionTokens());
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

        byte[] requestContent = mappingService.requestDtoToBytes(requestDto);
        log.debug("requestDto: " + mappingService.requestDtoToString(requestDto));

        return RequestBody.create(requestContent, MediaType.get("application/json"));
    }

    private ChatCompletionResponse buildResponseContent(String rawResponse) throws IOException {
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
