package me.lookforfps.oja.chatcompletion.mapping;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import me.lookforfps.oja.chatcompletion.model.natives.message.*;
import me.lookforfps.oja.chatcompletion.model.natives.message.content.*;
import me.lookforfps.oja.chatcompletion.model.natives.request.ChatCompletionRequestDto;
import me.lookforfps.oja.chatcompletion.model.natives.response.ChatCompletionResponse;
import me.lookforfps.oja.chatcompletion.model.natives.response.Choice;
import me.lookforfps.oja.chatcompletion.model.natives.logprobs.LogProbs;
import me.lookforfps.oja.chatcompletion.model.streaming.chunk.Chunk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MappingService {

    private final ObjectMapper objectMapper;
    private final ObjectWriter objectWriter;

    public MappingService() {
        this.objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
    }

    public String responseToString(ChatCompletionResponse response) throws JsonProcessingException {
        return objectWriter.writeValueAsString(response);
    }

    public String requestToString(ChatCompletionRequestDto requestDto) throws JsonProcessingException {
        return objectWriter.writeValueAsString(requestDto);
    }

    public byte[] requestDtoToBytes(ChatCompletionRequestDto requestDto) throws JsonProcessingException {
        return objectWriter.writeValueAsBytes(requestDto);
    }

    public ChatCompletionResponse bytesToResponse(byte[] bytes) throws IOException {
        return bytesToChatCompletionResponse(bytes);
    }

    public Chunk bytesToChunk(byte[] chunkBytes) throws IOException {
        return objectMapper.readValue(chunkBytes, Chunk.class);
    }

    private ChatCompletionResponse bytesToChatCompletionResponse(byte[] bytes) throws IOException {
        JsonNode responseJsonNode = objectMapper.readTree(bytes);
        List<Choice> choices = jsonNodesToChoiceList(responseJsonNode.get("choices"));

        ((ObjectNode) responseJsonNode).set("choices", objectMapper.readTree("[]"));
        bytes = objectMapper.writeValueAsBytes(responseJsonNode);

        ChatCompletionResponse response = objectMapper.readValue(bytes, ChatCompletionResponse.class);

        response.setChoices(choices);

        return response;
    }


    private List<Choice> jsonNodesToChoiceList(JsonNode choicesJsonNode) throws JsonProcessingException {
        List<Choice> choices = new ArrayList<>();
        if(choicesJsonNode.isArray()) {
            for(JsonNode choiceJsonNode : choicesJsonNode) {
                Choice choice = jsonNodeToChoice(choiceJsonNode);
                choices.add(choice);
            }
            return choices;
        } else {
            return null;
        }
    }

    private Choice jsonNodeToChoice(JsonNode choiceJsonNode) throws JsonProcessingException {
        Choice choice = new Choice();
        choice.setIndex(choiceJsonNode.get("index").asInt());
        if(choiceJsonNode.get("finish_reason") != null) {
            choice.setFinish_reason(choiceJsonNode.get("finish_reason").asText());
        }
        if (choiceJsonNode.get("logprobs") != null) {
            choice.setLogprobs(objectMapper.treeToValue(choiceJsonNode.get("logprobs"), LogProbs.class));
        }
        if(choiceJsonNode.get("message") != null) {
            choice.setMessage(jsonNodeToMessage(choiceJsonNode.get("message")));
        }

        return choice;
    }

    private Content jsonNodeToContent(JsonNode contentJsonNode) throws JsonProcessingException {
        Content content = new Content();

        if(contentJsonNode.isArray()) {
            for(JsonNode contentEntryJsonNode : contentJsonNode) {
                ContentEntry contentEntry = jsonNodeToContentEntry(contentEntryJsonNode);
                content.add(contentEntry);
            }
            return content;
        } else {
            return null;
        }
    }

    private ContentEntry jsonNodeToContentEntry(JsonNode contentEntryJsonNode) throws JsonProcessingException {
        ContentEntry contentEntry = new ContentEntry();

        ContentType contentType = ContentType.fromIdentifier(contentEntryJsonNode.get("type").asText());

        assert contentType != null;
        if(contentType.equals(ContentType.TEXT)) {
            contentEntry = objectMapper.treeToValue(contentEntryJsonNode, TextContent.class);
        }
        if(contentType.equals(ContentType.IMAGE_URL)) {
            contentEntry = objectMapper.treeToValue(contentEntryJsonNode, ImageContent.class);
        }
        if(contentType.equals(ContentType.REFUSAL)) {
            contentEntry = objectMapper.treeToValue(contentEntryJsonNode, RefusalContent.class);
        }

        return contentEntry;
    }

    private Message jsonNodeToMessage(JsonNode messageJsonNode) throws JsonProcessingException {
        MessageRole role = MessageRole.fromIdentifier(messageJsonNode.get("role").asText());
        assert role != null;
        if(role.equals(MessageRole.SYSTEM)) {
            return objectMapper.treeToValue(messageJsonNode, SystemMessage.class);
        } else if(role.equals(MessageRole.DEVELOPER)) {
            return objectMapper.treeToValue(messageJsonNode, DeveloperMessage.class);
        } else if(role.equals(MessageRole.USER)) {
            return objectMapper.treeToValue(messageJsonNode, UserMessage.class);
        } else if(role.equals(MessageRole.ASSISTANT)) {
            if(messageJsonNode.has("content")) {
                if(messageJsonNode.get("content").isArray()) {
                    Content content = jsonNodeToContent(messageJsonNode.get("content"));

                    ((ObjectNode) messageJsonNode).set("content", objectMapper.readTree("[]"));
                    AssistantMessage assistantMessage = objectMapper.treeToValue(messageJsonNode, AssistantMessage.class);
                    assistantMessage.setContent(content);

                    return assistantMessage;
                } else {
                    Content content = Content.createTextContent(messageJsonNode.get("content").asText());

                    ((ObjectNode) messageJsonNode).set("content", objectMapper.readTree("[]"));
                    AssistantMessage assistantMessage = objectMapper.treeToValue(messageJsonNode, AssistantMessage.class);
                    assistantMessage.setContent(content);

                    return assistantMessage;
                }
            } else {
                return objectMapper.treeToValue(messageJsonNode, AssistantMessage.class);
            }
        } else if(role.equals(MessageRole.TOOL)) {
            return objectMapper.treeToValue(messageJsonNode, ToolMessage.class);
        }
        return null;
    }
}
