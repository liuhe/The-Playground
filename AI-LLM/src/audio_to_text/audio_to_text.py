import models

class Word:
    def __init__(self, word: str, start: float, end: float):
        self.word = word
        self.start = start
        self.end = end

    def __repr__(self):
        return f"Word(word={self.word}, start={self.start}, end={self.end})"

def audio_to_text(filepath: str) -> list[Word]:
    groq = models.create_groq_model()

    with open(filepath, "rb") as file:
        transcription = groq.audio.transcriptions.create(
            model="whisper-large-v3-turbo",
            file=(filepath, file.read()),
            response_format="verbose_json",
            timestamp_granularities=["word"],
        )
    words = [
        Word(word=word["word"], start=word["start"], end=word["end"])
        for word in transcription.words
    ]
    return words
