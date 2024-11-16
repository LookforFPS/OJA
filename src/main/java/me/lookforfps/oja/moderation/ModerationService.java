package me.lookforfps.oja.moderation;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.lookforfps.oja.exception.InputNotSupportedException;
import me.lookforfps.oja.moderation.config.ModerationConfiguration;
import me.lookforfps.oja.moderation.mapping.MappingService;
import me.lookforfps.oja.moderation.model.input.MultiModalInput;
import me.lookforfps.oja.moderation.model.input.StringArrayInput;
import me.lookforfps.oja.moderation.model.input.StringInput;
import me.lookforfps.oja.moderation.model.input.inputentry.InputEntry;
import me.lookforfps.oja.moderation.model.request.ModerationRequestDto;
import me.lookforfps.oja.moderation.model.response.ModerationResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Slf4j
public class ModerationService {

    @Getter
    @Setter
    private ModerationConfiguration config;
    private final MappingService mappingService;

    private ModerationService(ModerationConfiguration configuration) {
        this.config = configuration;
        this.mappingService = new MappingService();
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
    public static ModerationService build(ModerationConfiguration configuration) {
        return build(configuration.getApiToken(), configuration.getModel(), configuration);
    }
    public static ModerationService build(String apiToken, String modelIdentifier) {
        return build(apiToken, modelIdentifier, null);
    }


    public ModerationResponse sendRequest(String input) throws IOException, InputNotSupportedException {
        ModerationRequestDto requestDto = buildInput(input);
        byte[] request = buildRequest(requestDto);

        return sendRequest(request);
    }
    public ModerationResponse sendRequest(List<?> input) throws IOException, InputNotSupportedException {
        ModerationRequestDto requestDto = buildInput(input);
        byte[] request = buildRequest(requestDto);

        return sendRequest(request);
    }
    private ModerationResponse sendRequest(byte[] request) throws IOException {
        HttpURLConnection con = buildConnection();

        con.getOutputStream().write(request);

        String output = new BufferedReader(new InputStreamReader(con.getInputStream())).lines()
                .reduce((a, b) -> a + b).get();

        con.disconnect();

        ModerationResponse response = buildResponse(output);

        return response;
    }

    private ModerationRequestDto buildInput(String input) throws InputNotSupportedException {
        if(input!=null) {
            return new StringInput(input);
        } else {
            throw new InputNotSupportedException("Moderation input is null! This is not supported!");
        }
    }
    private ModerationRequestDto buildInput(List<?> input) throws InputNotSupportedException {
        if(input!=null && !input.isEmpty()) {
            if(input.get(0).getClass() == String.class) {
                log.debug("String-Array input detected");
                return new StringArrayInput((List<String>) input);
            } else if(input.get(0).getClass() == InputEntry.class) {
                log.debug("Multi-Modal input detected");
                return new MultiModalInput((List<InputEntry>) input);
            } else {
                throw new InputNotSupportedException("Moderation input is not supported!");
            }
        } else {
            throw new InputNotSupportedException("Moderation input is null or empty! This is not supported!");
        }
    }

    private byte[] buildRequest(ModerationRequestDto requestDto) throws IOException {
        requestDto.setModel(config.getModel());

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

    private ModerationResponse buildResponse(String rawResponse) throws IOException {
        log.debug("rawResponse: "+rawResponse);

        ModerationResponse response = mappingService.bytesToResponse(rawResponse.getBytes());
        log.debug("processedResponse: "+ mappingService.responseToString(response));

        return response;
    }
}
