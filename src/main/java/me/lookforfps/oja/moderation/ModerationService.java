package me.lookforfps.oja.moderation;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.lookforfps.oja.aimodel.ModerationModel;
import me.lookforfps.oja.error.ErrorHandler;
import me.lookforfps.oja.error.exception.ApiErrorException;
import me.lookforfps.oja.error.exception.InputNotSupportedException;
import me.lookforfps.oja.error.exception.RequestErrorException;
import me.lookforfps.oja.moderation.config.ModerationConfiguration;
import me.lookforfps.oja.moderation.mapping.MappingService;
import me.lookforfps.oja.moderation.model.input.MultiModalInput;
import me.lookforfps.oja.moderation.model.input.StringArrayInput;
import me.lookforfps.oja.moderation.model.input.StringInput;
import me.lookforfps.oja.moderation.model.input.inputentry.ImageInputEntry;
import me.lookforfps.oja.moderation.model.input.inputentry.InputEntry;
import me.lookforfps.oja.moderation.model.input.inputentry.TextInputEntry;
import me.lookforfps.oja.moderation.model.request.ModerationRequestDto;
import me.lookforfps.oja.moderation.model.response.ModerationResponse;
import okhttp3.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

@Slf4j
public class ModerationService {

    @Getter
    @Setter
    private ModerationConfiguration config;

    private final MappingService mappingService;
    private final ErrorHandler errorHandler;

    private ModerationService(ModerationConfiguration configuration) {
        this.config = configuration;
        this.mappingService = new MappingService();
        this.errorHandler = new ErrorHandler();
    }

    public static ModerationService build(String apiToken, String modelIdentifier, ModerationConfiguration configuration) {
        if(configuration==null) {
            configuration = new ModerationConfiguration();
        }
        configuration.setApiToken(apiToken);
        configuration.setModel(modelIdentifier);
        if(configuration.getApiToken()!=null && configuration.getModel()!=null) {
            return new ModerationService(configuration);
        } else {
            log.error("The API Token or/and Model is not set!");
            return null;
        }
    }
    public static ModerationService build(String apiToken, ModerationModel model, ModerationConfiguration configuration) {
        return build(apiToken, model.getIdentifier(), configuration);
    }
    public static ModerationService build(ModerationConfiguration configuration) {
        return build(configuration.getApiToken(), configuration.getModel(), configuration);
    }
    public static ModerationService build(String apiToken, ModerationModel model) {
        return build(apiToken, model.getIdentifier(), null);
    }
    public static ModerationService build(String apiToken, String modelIdentifier) {
        return build(apiToken, modelIdentifier, null);
    }


    public ModerationResponse sendRequest(String input) throws ApiErrorException, RequestErrorException {
        ModerationRequestDto requestDto = buildInput(input);

        return sendRequest(requestDto);
    }
    public ModerationResponse sendRequest(List<?> input) throws ApiErrorException, RequestErrorException {
        ModerationRequestDto requestDto = buildInput(input);

        return sendRequest(requestDto);
    }
    private ModerationResponse sendRequest(ModerationRequestDto requestDto) throws ApiErrorException, RequestErrorException {
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

    private ModerationRequestDto buildInput(String input) {
        if(input!=null) {
            return new StringInput(input);
        } else {
            throw new InputNotSupportedException("Moderation input is null! This is not supported!");
        }
    }
    private ModerationRequestDto buildInput(List<?> input) {
        if(input!=null && !input.isEmpty()) {
            Object inputClass = input.get(0).getClass();
            if(inputClass == String.class) {
                log.debug("String-Array input detected");
                return new StringArrayInput((List<String>) input);
            } else if(inputClass == ImageInputEntry.class || inputClass == TextInputEntry.class) {
                log.debug("Multi-Modal input detected");
                return new MultiModalInput((List<InputEntry>) input);
            } else {
                throw new InputNotSupportedException("Moderation input is not supported!");
            }
        } else {
            throw new InputNotSupportedException("Moderation input is null or empty! This is not supported!");
        }
    }

    private Request buildRequest(RequestBody requestBody) {
        return new Request.Builder()
                .url(config.getApiUrl())
                .addHeader("Authorization", "Bearer " + config.getApiToken())
                .post(requestBody)
                .build();
    }

    private RequestBody buildRequestBody(ModerationRequestDto requestDto) throws IOException {
        requestDto.setModel(config.getModel());

        byte[] requestContent = mappingService.requestDtoToBytes(requestDto);
        log.debug("requestDto: " + mappingService.requestDtoToString(requestDto));

        return RequestBody.create(requestContent, MediaType.get("application/json"));
    }

    private ModerationResponse buildResponseContent(String rawResponse) throws IOException {
        log.debug("rawResponse: "+rawResponse);

        ModerationResponse response = mappingService.bytesToResponse(rawResponse.getBytes());
        log.debug("processedResponse: "+ mappingService.responseToString(response));

        return response;
    }
}
