package io.github.liuhe.ai_llm_playground.audio_to_text;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AudioToTextTest {
    @Autowired
    private AudioToText audioToText;

    @Test
    void testTranscribe() {
        String audioFilePath = "../src/audio_to_text/audio_to_text_test.m4a";
        System.out.println("Audio file path: " + audioFilePath);

        var words = audioToText.transcribe(audioFilePath);
        System.out.println("Transcription result:");
        for (var word : words) {
            System.out.format("%s ~ %s: %s\n", format(word.start()), format(word.end()), word.word());
        }
    }

    private static String format(Float time){
        if (time == null) {
            return "?";
        }

        int hours = (int) (time / 3600);
        int minutes = (int) (time / 60);
        int seconds = (int) (time % 60);

        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }
}
