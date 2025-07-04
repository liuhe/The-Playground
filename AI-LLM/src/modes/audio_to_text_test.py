import os
import unittest
import models
from . import audio_to_text

class TestAudioToText(unittest.TestCase):
    def test_audio_to_text(self):
        # Replace 'sample_audio.mp3' with a valid test audio file path
        test_file = os.path.dirname(__file__) + "/audio_to_text_test.m4a"
        groq = models.groq.create_groq_model()
        sub_words = audio_to_text.transcribe(groq, test_file)
        for word in sub_words:
            self.assertIsInstance(word.word, str)
            self.assertIsInstance(word.start, float)
            # self.assertIsInstance(word.end, float) # may not be float
            print(f"{time_range_to_string(word.start, word.end)} {word.word}")

def time_range_to_string(start: float, end: float) -> str:
    start_time = f"{int(start // 60):02d}:{int(start % 60):02d}"
    end_time = f"{int(end // 60):02d}:{int(end % 60):02d}"
    return f"{start_time} ~ {end_time}"

if __name__ == '__main__':
    unittest.main()
