package me.lookforfps.oja.chatcompletion;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.lookforfps.oja.chatcompletion.config.ChatCompletionConfiguration;
import me.lookforfps.oja.chatcompletion.entity.Message;
import me.lookforfps.oja.chatcompletion.entity.request.ChatCompletionRequestDto;
import me.lookforfps.oja.chatcompletion.entity.content.ContentList;
import me.lookforfps.oja.chatcompletion.entity.response.ChatCompletionResponse;
import me.lookforfps.oja.chatcompletion.entity.response.ChatCompletionResponseDto;
import me.lookforfps.oja.chatcompletion.entity.response.Choice;
import me.lookforfps.oja.chatcompletion.mapping.MappingService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ChatCompletion {

    @Getter
    @Setter
    private ChatCompletionConfiguration config;
    @Getter
    @Setter
    private List<Message> messages = new ArrayList<>();
    private MappingService mappingService;

    public ChatCompletion(ChatCompletionConfiguration configuration) {
        this.config = configuration;

        this.mappingService = new MappingService();
    }

    public ChatCompletionResponse sendRequest() throws IOException {
        ChatCompletionRequestDto request = buildRequest();

        String url = "https://api.openai.com/v1/chat/completions";
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer "+config.getApiToken());

        con.setDoOutput(true);
        con.getOutputStream().write(mappingService.requestDtoToBytes(request));

        log.info("request: "+mappingService.requestDtoToString(request));

        String output = new BufferedReader(new InputStreamReader(con.getInputStream())).lines()
                .reduce((a, b) -> a + b).get();

        log.info("response: "+output);

        ChatCompletionResponseDto response = mappingService.bytesToResponseDto(output.getBytes(), ChatCompletionResponseDto.class);

        log.info("processedResponse: "+mappingService.responseDtoToString(response));

        if(config.isAutoAddAIResponseToContext()) {
            addMessage(response.getChoices().get(0).getMessage());
        }

        return buildResponse(response);
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
