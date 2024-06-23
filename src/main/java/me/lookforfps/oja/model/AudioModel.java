package me.lookforfps.oja.model;

import lombok.Getter;
import me.lookforfps.oja.entity.Model;

import java.util.Arrays;

@Getter
public enum AudioModel {
    /**
     * <b>Description: </b>
     * Whisper is a general-purpose speech recognition model. It is trained on a large dataset of diverse audio and is also a multi-task model that can perform multilingual speech recognition as well as speech translation and language identification.
     */
    WHISPER_1(new Model("whisper-1", "Whisper-1")),
    /**
     * <b>Description: </b>
     * The latest text to speech model, optimized for speed.
     */
    TTS_1(new Model("tts-1", "TTS-1")),
    /**
     * <b>Description: </b>
     * The latest text to speech model, optimized for quality.
     */
    TTS_1_HD(new Model("tts-1-hd", "TTS-1-HD"));

    private Model Model;

    AudioModel(Model Model) {
        this.Model = Model;
    }

    public static AudioModel getModelByIdentifier(String identifier) {
        return Arrays.stream(AudioModel.values()).filter(aiModel -> aiModel.getModel().getIdentifier().equals(identifier)).findFirst().orElse(null);
    }

    public static AudioModel getModelByLabel(String name) {
        return Arrays.stream(AudioModel.values()).filter(aiModel -> aiModel.getModel().getLabel().equals(name)).findFirst().orElse(null);
    }
}
