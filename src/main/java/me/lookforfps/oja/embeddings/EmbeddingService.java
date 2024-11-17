package me.lookforfps.oja.embeddings;

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
import me.lookforfps.oja.exception.InputNotSupportedException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Slf4j
public class EmbeddingService {

    @Getter
    @Setter
    private EmbeddingConfiguration config;
    private final MappingService mappingService;

    private EmbeddingService(EmbeddingConfiguration configuration) {
        this.config = configuration;
        this.mappingService = new MappingService();
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


    public EmbeddingResponse sendRequest(String input) throws IOException, InputNotSupportedException {
        EmbeddingRequestDto requestDto = buildInput(input);
        byte[] request = buildRequest(requestDto);

        return sendRequest(request);
    }
    public EmbeddingResponse sendRequest(List<?> input) throws IOException, InputNotSupportedException {
        EmbeddingRequestDto requestDto = buildInput(input);
        byte[] request = buildRequest(requestDto);

        return sendRequest(request);
    }
    private EmbeddingResponse sendRequest(byte[] request) throws IOException {
        HttpURLConnection con = buildConnection();

        con.getOutputStream().write(request);

        String output = new BufferedReader(new InputStreamReader(con.getInputStream())).lines()
                .reduce((a, b) -> a + b).get();

        con.disconnect();

        EmbeddingResponse response = buildResponse(output);

        return response;
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

    private byte[] buildRequest(EmbeddingRequestDto requestDto) throws IOException {
        requestDto.setModel(config.getModel());

        requestDto.setDimensions(config.getDimensions());
        if(config.getEncodingFormat() != null) {
            requestDto.setEncoding_format(config.getEncodingFormat().getIdentifier());
        }
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

    private EmbeddingResponse buildResponse(String rawResponse) throws IOException {
        log.debug("rawResponse: "+rawResponse);

        EmbeddingResponse response = mappingService.bytesToResponse(rawResponse.getBytes());
        log.debug("processedResponse: "+ mappingService.responseToString(response));

        return response;
    }
}
