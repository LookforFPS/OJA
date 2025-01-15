package me.lookforfps.oja.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.lookforfps.oja.chatcompletion.hook.StreamContainer;
import me.lookforfps.oja.chatcompletion.hook.StreamEmitter;
import me.lookforfps.oja.error.exception.ApiErrorException;
import me.lookforfps.oja.error.exception.RequestErrorException;
import okhttp3.Response;

import java.io.IOException;

@Slf4j
public class ErrorHandler {

    private final ObjectMapper objectMapper;

    public ErrorHandler() {
        this.objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public void handleRequestError(Exception ex) {
        throw new RequestErrorException(ex);
    }

    public void handleApiError(Response response) {
        try {
            JsonNode errorNode = jsonStringToJsonNode(response.body().string());
            ApiErrorException exception = jsonNodeToApiError(errorNode);

            log.error("API-Side-Error during processing request: ("+exception.getType()+") "+exception.getMessage());
            throw exception;
        } catch (IOException ex) {
            log.warn("Error during processing error object: {}", ex.getMessage());
            log.error("API-Side-Error during processing request: {}", response.body().toString());
        }
    }

    public void handleRequestError(Exception ex, StreamContainer streamContainer) {
        RequestErrorException exception = new RequestErrorException(ex);

        StreamEmitter.emitStreamFailed(exception, streamContainer.getListeners());
    }

    public void handleApiError(Response response, StreamContainer streamContainer) {
        try {
            JsonNode errorNode = jsonStringToJsonNode(response.body().string());
            ApiErrorException exception = jsonNodeToApiError(errorNode);

            log.error("API-Side-Error during processing request: ("+exception.getType()+") "+exception.getMessage());

            StreamEmitter.emitStreamFailed(exception, streamContainer.getListeners());
        } catch (IOException ex) {
            log.warn("Error during processing error object: {}", ex.getMessage());
            log.error("API-Side-Error during processing request: {}", response.body().toString());
        }
    }

    private JsonNode jsonStringToJsonNode(String jsonString) throws JsonProcessingException {
        return objectMapper.readTree(jsonString);
    }

    private ApiErrorException jsonNodeToApiError(JsonNode node) {
        node = node.get("error");

        String message = node.has("message") ? node.get("message").asText() : null;
        String type = node.has("type") ? node.get("type").asText() : null;
        String param = node.has("param") ? node.get("param").asText() : null;
        String code = node.has("code") ? node.get("code").asText() : null;

        return new ApiErrorException(message, type, param, code);
    }
}
