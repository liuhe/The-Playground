package io.github.liuhe.ai_llm_playground.audio_to_text;

import java.util.List;

import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi.StructuredResponse.Word;
import org.springframework.ai.openai.api.OpenAiAudioApi.TranscriptResponseFormat;
import org.springframework.ai.openai.api.OpenAiAudioApi.TranscriptionRequest.GranularityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

@Component
public class AudioToText {
    @Autowired
    private OpenAiAudioTranscriptionModel_Structured model;

    public List<Word> transcribe(String audioFilePath) {
        AudioTranscriptionPrompt prompt = new AudioTranscriptionPrompt(
                new FileSystemResource(audioFilePath),
                OpenAiAudioTranscriptionOptions.builder()
                        .temperature(0.7f)
                        .responseFormat(TranscriptResponseFormat.VERBOSE_JSON)
                        .granularityType(GranularityType.WORD)
                        .build());
        var rs = model.call(prompt);
        var structuredResponse = rs.getRight();
        if (structuredResponse == null) {
            throw new RuntimeException("Failed to transcribe audio");
        }
        return structuredResponse.words();
    }
}
