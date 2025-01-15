package me.lookforfps.oja.moderation.mapping;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import me.lookforfps.oja.moderation.model.request.ModerationRequestDto;
import me.lookforfps.oja.moderation.model.response.ModerationResponse;
import me.lookforfps.oja.moderation.model.results.Categories;
import me.lookforfps.oja.moderation.model.results.CategoryAppliedInputTypes;
import me.lookforfps.oja.moderation.model.results.CategoryScores;
import me.lookforfps.oja.moderation.model.results.Result;

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

    public String requestDtoToString(ModerationRequestDto requestDto) throws JsonProcessingException {
        return objectWriter.writeValueAsString(requestDto);
    }

    public byte[] requestDtoToBytes(ModerationRequestDto requestDto) throws JsonProcessingException {
        return objectWriter.writeValueAsBytes(requestDto);
    }

    public String responseToString(ModerationResponse response) throws JsonProcessingException {
        return objectWriter.writeValueAsString(response);
    }

    public ModerationResponse bytesToResponse(byte[] bytes) throws IOException {
        JsonNode responseNode = objectMapper.readTree(bytes);

        List<Result> results = jsonNodeToResults(responseNode);

        ((ObjectNode) responseNode).set("results", objectMapper.readTree("null"));
        bytes = objectMapper.writeValueAsBytes(responseNode);

        ModerationResponse response = objectMapper.readValue(bytes, ModerationResponse.class);

        response.setAllResults(results);

        return response;
    }

    private List<Result> jsonNodeToResults(JsonNode node) throws IOException {
        JsonNode resultsNode = node.get("results");

        List<Result> results = new ArrayList<>();

        for(JsonNode resultNode : resultsNode) {
            Result result = new Result();

            Categories categories = jsonNodeToCategories(resultNode);
            CategoryScores categoryScores = jsonNodeToCategoryScores(resultNode);
            CategoryAppliedInputTypes categoryAppliedInputTypes = jsonNodeToCategoryAppliedinputTypes(resultNode);

            result.setFlagged(resultNode.get("flagged").asBoolean());
            result.setCategories(categories);
            result.setCategoryScores(categoryScores);
            result.setCategoryAppliedInputTypes(categoryAppliedInputTypes);

            results.add(result);
        }

        return results;
    }

    private Categories jsonNodeToCategories(JsonNode node) {
        JsonNode categoriesNode = node.get("categories");

        Categories categories = new Categories();

        categories.setSexual(categoriesNode.get("sexual").asBoolean());
        categories.setSexual_minors(categoriesNode.get("sexual/minors").asBoolean());
        categories.setHate(categoriesNode.get("hate").asBoolean());
        categories.setHate_threatening(categoriesNode.get("hate/threatening").asBoolean());
        categories.setHarassment(categoriesNode.get("harassment").asBoolean());
        categories.setHarassment_threatening(categoriesNode.get("harassment/threatening").asBoolean());
        categories.setSelfHarm(categoriesNode.get("self-harm").asBoolean());
        categories.setSelfHarm_intent(categoriesNode.get("self-harm/intent").asBoolean());
        categories.setSelfHarm_instructions(categoriesNode.get("self-harm/instructions").asBoolean());
        categories.setViolence(categoriesNode.get("violence").asBoolean());
        categories.setViolence_graphic(categoriesNode.get("violence/graphic").asBoolean());
        categories.setIllicit(categoriesNode.get("illicit").asBoolean());
        categories.setIllicit_violent(categoriesNode.get("illicit/violent").asBoolean());

        return categories;
    }

    private CategoryScores jsonNodeToCategoryScores(JsonNode node) {
        JsonNode categoryScoresNode = node.get("category_scores");

        CategoryScores categoryScores = new CategoryScores();

        categoryScores.setSexual((float) categoryScoresNode.get("sexual").asDouble());
        categoryScores.setSexual_minors((float) categoryScoresNode.get("sexual/minors").asDouble());
        categoryScores.setHate((float) categoryScoresNode.get("hate").asDouble());
        categoryScores.setHate_threatening((float) categoryScoresNode.get("hate/threatening").asDouble());
        categoryScores.setHarassment((float) categoryScoresNode.get("harassment").asDouble());
        categoryScores.setHarassment_threatening((float) categoryScoresNode.get("harassment/threatening").asDouble());
        categoryScores.setSelfHarm((float) categoryScoresNode.get("self-harm").asDouble());
        categoryScores.setSelfHarm_intent((float) categoryScoresNode.get("self-harm/intent").asDouble());
        categoryScores.setSelfHarm_instructions((float) categoryScoresNode.get("self-harm/instructions").asDouble());
        categoryScores.setViolence((float) categoryScoresNode.get("violence").asDouble());
        categoryScores.setViolence_graphic((float) categoryScoresNode.get("violence/graphic").asDouble());
        categoryScores.setIllicit((float) categoryScoresNode.get("illicit").asDouble());
        categoryScores.setIllicit_violent((float) categoryScoresNode.get("illicit/violent").asDouble());

        return categoryScores;
    }

    private CategoryAppliedInputTypes jsonNodeToCategoryAppliedinputTypes(JsonNode node) throws IOException {
        JsonNode categoryAppliedInputTypesNode = node.get("category_applied_input_types");
        ObjectReader objectReader = objectMapper.readerFor(new TypeReference<List<String>>(){});

        CategoryAppliedInputTypes categoryAppliedInputTypes = new CategoryAppliedInputTypes();

        categoryAppliedInputTypes.setSexual(objectReader.readValue(categoryAppliedInputTypesNode.get("sexual")));
        categoryAppliedInputTypes.setSexual_minors(objectReader.readValue(categoryAppliedInputTypesNode.get("sexual/minors")));
        categoryAppliedInputTypes.setHate(objectReader.readValue(categoryAppliedInputTypesNode.get("hate")));
        categoryAppliedInputTypes.setHate_threatening(objectReader.readValue(categoryAppliedInputTypesNode.get("hate/threatening")));
        categoryAppliedInputTypes.setHarassment(objectReader.readValue(categoryAppliedInputTypesNode.get("harassment")));
        categoryAppliedInputTypes.setHarassment_threatening(objectReader.readValue(categoryAppliedInputTypesNode.get("harassment/threatening")));
        categoryAppliedInputTypes.setSelfHarm(objectReader.readValue(categoryAppliedInputTypesNode.get("self-harm")));
        categoryAppliedInputTypes.setSelfHarm_intent(objectReader.readValue(categoryAppliedInputTypesNode.get("self-harm/intent")));
        categoryAppliedInputTypes.setSelfHarm_instructions(objectReader.readValue(categoryAppliedInputTypesNode.get("self-harm/instructions")));
        categoryAppliedInputTypes.setViolence(objectReader.readValue(categoryAppliedInputTypesNode.get("violence")));
        categoryAppliedInputTypes.setViolence_graphic(objectReader.readValue(categoryAppliedInputTypesNode.get("violence/graphic")));
        categoryAppliedInputTypes.setIllicit(objectReader.readValue(categoryAppliedInputTypesNode.get("illicit")));
        categoryAppliedInputTypes.setIllicit_violent(objectReader.readValue(categoryAppliedInputTypesNode.get("illicit/violent")));

        return categoryAppliedInputTypes;
    }

}
