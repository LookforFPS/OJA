package me.lookforfps.oja.aimodel;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum AudioModel {
    /**
     * <b>Description: </b>
     * Whisper is a general-purpose speech recognition model. It is trained on a large dataset of diverse audio and is also a multi-task model that can perform multilingual speech recognition as well as speech translation and language identification.
     */
    WHISPER_1(new AIModel("whisper-1", "Whisper-1")),
    /**
     * <b>Description: </b>
     * The latest text to speech model, optimized for speed.
     */
    TTS_1(new AIModel("tts-1", "TTS-1")),
    /**
     * <b>Description: </b>
     * The latest text to speech model, optimized for quality.
     */
    TTS_1_HD(new AIModel("tts-1-hd", "TTS-1-HD"));

    private AIModel AIModel;

    AudioModel(AIModel AIModel) {
        this.AIModel = AIModel;
    }
}
