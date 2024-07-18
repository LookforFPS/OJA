package me.lookforfps.oja.aimodel;

import lombok.Getter;

@Getter
public enum AudioModel {
    /**
     * <b>Description: </b>
     * Whisper is a general-purpose speech recognition model. It is trained on a large dataset of diverse audio and is also a multi-task model that can perform multilingual speech recognition as well as speech translation and language identification.
     */
    WHISPER_1("whisper-1"),
    /**
     * <b>Description: </b>
     * The latest text to speech model, optimized for speed.
     */
    TTS_1("tts-1"),
    /**
     * <b>Description: </b>
     * The latest text to speech model, optimized for quality.
     */
    TTS_1_HD("tts-1-hd");

    private String identifier;

    AudioModel(String identifier) {
        this.identifier = identifier;
    }
}
