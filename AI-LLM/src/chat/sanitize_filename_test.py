import unittest
from .sanitize_filename import sanitize_filename

# From the test case below, it can be seen that large models are not easy to control
class TestSanitizeFilename(unittest.TestCase):
    def test_remove_invalid_characters(self):
        self.assertEqual(sanitize_filename("file:name?.txt"), "file_name_.txt")
        self.assertEqual(sanitize_filename("inva|lid*chars<>.txt"), "invalid_chars.txt")

    def test_trim_whitespace(self):
        self.assertEqual(sanitize_filename("   file name   .txt   "), "file_name.txt")

    def test_empty_filename(self):
        self.assertEqual(sanitize_filename(""), "untitled")

    def test_reserved_names(self):
        self.assertEqual(sanitize_filename("CON.txt"), "_CON.txt")
        self.assertEqual(sanitize_filename("AUX"), "_AUX")

    def test_long_filename(self):
        long_name = "a" * 300 + ".txt"
        sanitized = sanitize_filename(long_name)
        self.assertTrue(len(sanitized) <= 255)

    def test_chinese_characters(self):
        self.assertEqual(sanitize_filename("文件名.txt"), "wen_jian_ming.txt")

    def test_other_language_characters(self):
        self.assertEqual(sanitize_filename("файл.txt"), "file.txt")
        self.assertEqual(sanitize_filename("ファイル名.txt"), "fuxairu_ming.txt")

if __name__ == "__main__":
    unittest.main()
