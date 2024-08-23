package me.lookforfps.oja.aimodel;

import lombok.Getter;

@Getter
public enum AudioModel {

    WHISPER_1("whisper-1"),
    TTS_1("tts-1"),
    TTS_1_HD("tts-1-hd");

    private String identifier;

    AudioModel(String identifier) {
        this.identifier = identifier;
    }
}
