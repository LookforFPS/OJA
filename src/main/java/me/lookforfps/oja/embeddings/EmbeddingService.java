package me.lookforfps.oja.embeddings;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.lookforfps.oja.aimodel.EmbeddingModel;
import me.lookforfps.oja.aimodel.ModerationModel;
import me.lookforfps.oja.embeddings.mapping.MappingService;
import me.lookforfps.oja.embeddings.config.EmbeddingConfiguration;
import me.lookforfps.oja.embeddings.model.input.NestedIntegerArrayInput;
import me.lookforfps.oja.embeddings.model.input.IntegerArrayInput;
import me.lookforfps.oja.embeddings.model.input.StringArrayInput;
import me.lookforfps.oja.embeddings.model.input.StringInput;
import me.lookforfps.oja.embeddings.model.request.EmbeddingRequestDto;
import me.lookforfps.oja.embeddings.model.response.EmbeddingResponse;
import me.lookforfps.oja.error.ErrorHandler;
import me.lookforfps.oja.error.exception.ApiErrorException;
import me.lookforfps.oja.error.exception.InputNotSupportedException;
import me.lookforfps.oja.error.exception.RequestErrorException;
import okhttp3.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

@Slf4j
public class EmbeddingService {

    @Getter
    @Setter
    private EmbeddingConfiguration config;

    private final MappingService mappingService;
    private final ErrorHandler errorHandler;

    private EmbeddingService(EmbeddingConfiguration configuration) {
        this.config = configuration;
        this.mappingService = new MappingService();
        this.errorHandler = new ErrorHandler();
    }

    public static EmbeddingService build(String apiToken, String modelIdentifier, EmbeddingConfiguration configuration) {
        if(configuration==null) {
            configuration = new EmbeddingConfiguration();
        }
        configuration.setApiToken(apiToken);
        configuration.setModel(modelIdentifier);
        if(configuration.getApiToken()!=null && configuration.getModel()!=null) {
            return new EmbeddingService(configuration);
        } else {
            log.error("The API Token or/and Model is not set!");
            return null;
        }
    }
    public static EmbeddingService build(String apiToken, ModerationModel model, EmbeddingConfiguration configuration) {
        return build(apiToken, model.getIdentifier(), configuration);
    }
    public static EmbeddingService build(EmbeddingConfiguration configuration) {
        return build(configuration.getApiToken(), configuration.getModel(), configuration);
    }
    public static EmbeddingService build(String apiToken, EmbeddingModel model) {
        return build(apiToken, model.getIdentifier(), null);
    }
    public static EmbeddingService build(String apiToken, String modelIdentifier) {
        return build(apiToken, modelIdentifier, null);
    }


    public EmbeddingResponse sendRequest(String input) throws ApiErrorException, RequestErrorException {
        EmbeddingRequestDto requestDto = buildInput(input);

        return sendRequest(requestDto);
    }
    public EmbeddingResponse sendRequest(List<?> input) throws ApiErrorException, RequestErrorException {
        EmbeddingRequestDto requestDto = buildInput(input);

        return sendRequest(requestDto);
    }
    private EmbeddingResponse sendRequest(EmbeddingRequestDto requestDto) throws ApiErrorException, RequestErrorException {
        OkHttpClient client = new OkHttpClient();

        try {
            RequestBody requestBody = buildRequestBody(requestDto);
            Request request = buildRequest(requestBody);

            Response response = client.newCall(request).execute();

            if(response.code() == 200) {
                return buildResponseContent(response.body().string());
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

    private EmbeddingRequestDto buildInput(String input) throws InputNotSupportedException {
        if(input!=null) {
            return new StringInput(input);
        } else {
            throw new InputNotSupportedException("Embedding input is null! This is not supported!");
        }
    }
    private EmbeddingRequestDto buildInput(List<?> input) throws InputNotSupportedException {
        if(input!=null && !input.isEmpty()) {
            if(input.get(0).getClass() == String.class) {
                log.debug("String-Array input detected");
                return new StringArrayInput((List<String>) input);
            } else if(input.get(0).getClass() == Integer.class) {
                log.debug("Integer-Array input detected");
                return new IntegerArrayInput((List<Integer>) input);
            } else if(input.get(0) instanceof List<?>) {
                List<?> innerArray = ((List<?>) input.get(0));
                if(innerArray!=null && !innerArray.isEmpty()) {
                    if(innerArray.get(0).getClass() == Integer.class) {
                        log.debug("Nested-Integer-Array input detected");
                        return new NestedIntegerArrayInput((List<List<Integer>>) input);
                    } else {
                        throw new InputNotSupportedException("Embedding input is not supported!");
                    }
                } else {
                    throw new InputNotSupportedException("Nested embedding input array is null or empty! This is not supported!");
                }
            } else {
                throw new InputNotSupportedException("Embedding input is not supported!");
            }
        } else {
            throw new InputNotSupportedException("Embedding input is null or empty! This is not supported!");
        }
    }

    private Request buildRequest(RequestBody requestBody) {
        return new Request.Builder()
                .url(config.getApiUrl())
                .addHeader("Authorization", "Bearer " + config.getApiToken())
                .post(requestBody)
                .build();
    }

    private RequestBody buildRequestBody(EmbeddingRequestDto requestDto) throws JsonProcessingException {
        requestDto.setModel(config.getModel());

        requestDto.setDimensions(config.getDimensions());
        if(config.getEncodingFormat() != null) {
            requestDto.setEncoding_format(config.getEncodingFormat().getIdentifier());
        }
        requestDto.setUser(config.getUser());

        byte[] requestContent = mappingService.requestDtoToBytes(requestDto);
        log.debug("requestDto: " + mappingService.requestDtoToString(requestDto));

        return RequestBody.create(requestContent, MediaType.get("application/json"));
    }

    private EmbeddingResponse buildResponseContent(String rawResponse) throws IOException {
        log.debug("rawResponse: "+rawResponse);

        EmbeddingResponse response = mappingService.bytesToResponse(rawResponse.getBytes());
        log.debug("processedResponse: "+ mappingService.responseToString(response));

        return response;
    }
}
