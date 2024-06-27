package me.lookforfps.oja.chatcompletion.mapping;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import me.lookforfps.oja.chatcompletion.model.natives.message.Message;
import me.lookforfps.oja.chatcompletion.model.natives.content.ContentList;
import me.lookforfps.oja.chatcompletion.model.natives.response.ChatCompletionResponseDto;
import me.lookforfps.oja.chatcompletion.model.natives.response.Choice;
import me.lookforfps.oja.chatcompletion.model.natives.logprobs.LogProbs;
import me.lookforfps.oja.chatcompletion.model.streaming.chunk.Chunk;
import me.lookforfps.oja.chatcompletion.model.streaming.chunk.ChunkDto;
import me.lookforfps.oja.chatcompletion.model.natives.toolcall.ToolCall;
import me.lookforfps.oja.model.RequestDto;
import me.lookforfps.oja.model.ResponseDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MappingService {

    ObjectMapper mapper;
    ObjectWriter writer;

    public MappingService() {
        this.mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        this.writer = mapper.writerWithDefaultPrettyPrinter();
    }

    public String responseDtoToString(ResponseDto responseDto) throws JsonProcessingException {
        return writer.writeValueAsString(responseDto);
    }

    public String requestDtoToString(RequestDto requestDto) throws JsonProcessingException {
        return writer.writeValueAsString(requestDto);
    }

    public byte[] requestDtoToBytes(RequestDto requestDto) throws JsonProcessingException {
        return writer.writeValueAsBytes(requestDto);
    }

    public <T> T bytesToResponseDto(byte[] bytes, Class<T> responseType) throws IOException {
        if(responseType.equals(ChatCompletionResponseDto.class)) {
            return (T) bytesToChatCompletionResponseDto(bytes);
        }

        return mapper.readValue(bytes, responseType);
    }

    private ChatCompletionResponseDto bytesToChatCompletionResponseDto(byte[] bytes) throws IOException {
        JsonNode responseJsonNode = mapper.readTree(bytes);
        List<Choice> choices = jsonNodesToChoiceList(responseJsonNode.get("choices"));

        ((ObjectNode) responseJsonNode).set("choices", mapper.readTree("[]"));
        bytes = mapper.writeValueAsBytes(responseJsonNode);

        ChatCompletionResponseDto responseDto = mapper.readValue(bytes, ChatCompletionResponseDto.class);

        responseDto.setChoices(choices);

        return responseDto;
    }


    public List<Choice> jsonNodesToChoiceList(JsonNode choicesJsonNode) throws JsonProcessingException {
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

    public Choice jsonNodeToChoice(JsonNode choiceJsonNode) throws JsonProcessingException {
        Choice choice = new Choice();
        choice.setIndex(choiceJsonNode.get("index").asInt());
        if(choiceJsonNode.get("finish_reason") != null) {
            choice.setFinish_reason(choiceJsonNode.get("finish_reason").asText());
        }
        if (choiceJsonNode.get("logprobs") != null) {
            choice.setLogprobs(mapper.treeToValue(choiceJsonNode.get("logprobs"), LogProbs.class));
        }
        if(choiceJsonNode.get("message") != null) {
            choice.setMessage(jsonNodeToMessage(choiceJsonNode.get("message")));
        }

        return choice;
    }

    public Message jsonNodeToMessage(JsonNode messageJsonNode) throws JsonProcessingException {
        Message message = new Message();
        message.setRole(messageJsonNode.get("role").asText());
        message.setContent(ContentList.addTextContent(messageJsonNode.get("content").asText()));
        if(messageJsonNode.get("name") != null) {
            message.setName(messageJsonNode.get("name").asText());
        }
        if(messageJsonNode.get("tool_calls") != null) {
            message.setTool_calls(mapper.treeToValue(messageJsonNode.get("tool_calls"), new TypeReference<List<ToolCall>>() {}));
        }
        if(messageJsonNode.get("tool_call_id") != null) {
            message.setTool_call_id((messageJsonNode.get("tool_call_id").asText()));
        }
        return message;
    }

    public Chunk bytesToChunk(byte[] chunkBytes) throws IOException {
        ChunkDto chunkDto = mapper.readValue(chunkBytes, ChunkDto.class);
        return chunkDtoToChunk(chunkDto);
    }

    public Chunk chunkDtoToChunk(ChunkDto chunkDto) {
        Chunk chunk = new Chunk();
        chunk.setId(chunkDto.getId());
        chunk.setUsedModel(chunkDto.getModel());
        chunk.setObject(chunkDto.getObject());
        chunk.setCreated(chunkDto.getCreated());
        chunk.setServiceTier(chunkDto.getService_tier());
        chunk.setUsage(chunkDto.getUsage());
        chunk.setSystemFingerprint(chunkDto.getSystem_fingerprint());
        chunk.setChoices(chunkDto.getChoices());

        return chunk;
    }
}
