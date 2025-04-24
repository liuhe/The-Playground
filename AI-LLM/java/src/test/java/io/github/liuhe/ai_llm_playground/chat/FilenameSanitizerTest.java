package io.github.liuhe.ai_llm_playground.chat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FilenameSanitizerTest {
    @Autowired
    private FilenameSanitizer filenameSanitizer;

    @Test
    void testSanitizeFile() {
        String[] filenames = new String[]{
            "file:name?.txt",
            "inva|lid*chars<>.txt",
            "   file name   .txt   ",
            "文件名.txt",
            "файл.txt",
            "ファイル名.txt",
        };
        for (String filename : filenames) {
            String sanitizedFilename = filenameSanitizer.sanitizeFile(filename);
            System.out.printf("\"%s\" -> \"%s\"\n", filename, sanitizedFilename);
        }
    }
}
