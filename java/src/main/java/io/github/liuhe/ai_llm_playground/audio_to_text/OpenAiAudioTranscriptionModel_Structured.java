package io.github.liuhe.ai_llm_playground.audio_to_text;

import java.lang.reflect.Method;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.ai.audio.transcription.AudioTranscription;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.chat.metadata.RateLimit;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.api.OpenAiAudioApi.StructuredResponse;
import org.springframework.ai.openai.metadata.audio.OpenAiAudioTranscriptionResponseMetadata;
import org.springframework.ai.openai.metadata.support.OpenAiResponseHeaderExtractor;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

/**
 * Since OpenAiAudioTranscriptionModel cannot return JSON with timestamps,
 * here's a hack.
 */
@Component
public class OpenAiAudioTranscriptionModel_Structured {
    private OpenAiAudioTranscriptionModel model;

    private final RetryTemplate retryTemplate;
    private final OpenAiAudioApi audioApi;

    private final Method createRequestMethod;

    public OpenAiAudioTranscriptionModel_Structured(OpenAiAudioTranscriptionModel model) {
        try {
            this.model = model;

            var retryTemplateField = model.getClass().getDeclaredField("retryTemplate");
            retryTemplateField.setAccessible(true);
            retryTemplate = (RetryTemplate) retryTemplateField.get(model);

            var audioApiField = model.getClass().getDeclaredField("audioApi");
            audioApiField.setAccessible(true);
            audioApi = (OpenAiAudioApi) audioApiField.get(model);

            createRequestMethod = model.getClass().getDeclaredMethod("createRequest",
                    AudioTranscriptionPrompt.class);
            createRequestMethod.setAccessible(true);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to access fields", ex);
        }
    }

    public Pair<AudioTranscriptionResponse, StructuredResponse> call(
            AudioTranscriptionPrompt prompt) {
        try {
            OpenAiAudioApi.TranscriptionRequest request = (OpenAiAudioApi.TranscriptionRequest) createRequestMethod
                    .invoke(model, prompt);

            if (!request.responseFormat().isJsonType()) {
                throw new IllegalArgumentException("Response format must be JSON type");
            }

            ResponseEntity<StructuredResponse> transcriptionEntity = retryTemplate
                    .execute(ctx -> audioApi.createTranscription(request, StructuredResponse.class));

            var transcription = transcriptionEntity.getBody();

            if (transcription == null) {
                return Pair.of(new AudioTranscriptionResponse(null), null);
            }

            AudioTranscription transcript = new AudioTranscription(transcription.text());

            RateLimit rateLimits = OpenAiResponseHeaderExtractor.extractAiResponseHeaders(transcriptionEntity);

            return Pair.of(new AudioTranscriptionResponse(transcript,
                    OpenAiAudioTranscriptionResponseMetadata.from(transcriptionEntity.getBody())
                            .withRateLimit(rateLimits)),
                    transcription);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to invoke createRequest method", ex);
        }
    }
}
