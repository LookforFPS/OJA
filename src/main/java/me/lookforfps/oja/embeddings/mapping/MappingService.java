package me.lookforfps.oja.embeddings.mapping;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import me.lookforfps.oja.embeddings.model.data.Base64Data;
import me.lookforfps.oja.embeddings.model.data.Data;
import me.lookforfps.oja.embeddings.model.data.FloatData;
import me.lookforfps.oja.embeddings.model.request.EmbeddingRequestDto;
import me.lookforfps.oja.embeddings.model.response.EmbeddingResponse;

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

    public String requestDtoToString(EmbeddingRequestDto requestDto) throws JsonProcessingException {
        return objectWriter.writeValueAsString(requestDto);
    }

    public byte[] requestDtoToBytes(EmbeddingRequestDto requestDto) throws JsonProcessingException {
        return objectWriter.writeValueAsBytes(requestDto);
    }

    public String responseToString(EmbeddingResponse response) throws JsonProcessingException {
        return objectWriter.writeValueAsString(response);
    }

    public EmbeddingResponse bytesToResponse(byte[] bytes) throws IOException {
        JsonNode responseNode = objectMapper.readTree(bytes);
        JsonNode dataListNode = responseNode.get("data");
        List<Data> dataList = jsonNodeToDataList(dataListNode);

        ((ObjectNode) responseNode).set("data", objectMapper.readTree("null"));
        bytes = objectMapper.writeValueAsBytes(responseNode);

        EmbeddingResponse response = objectMapper.readValue(bytes, EmbeddingResponse.class);

        response.setData(dataList);

        return response;
    }

    private List<Data> jsonNodeToDataList(JsonNode dataListNode) throws JsonProcessingException {
        List<Data> dataList = new ArrayList<>();

        for(JsonNode dataNode: dataListNode) {
            JsonNode embeddingNode = dataNode.get("embedding");
            if(embeddingNode.isArray()) {
                dataList.add(new FloatData(objectMapper.treeToValue(embeddingNode, new TypeReference<ArrayList<Float>>(){}), dataNode.get("index").asInt(), dataNode.get("object").asText()));
            } else {
                dataList.add(new Base64Data(embeddingNode.asText(), dataNode.get("index").asInt(), dataNode.get("object").asText()));
            }
        }

        return dataList;
    }

}
