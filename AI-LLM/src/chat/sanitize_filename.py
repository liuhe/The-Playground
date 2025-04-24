import models
from langchain_core.messages import (
    HumanMessage,
    SystemMessage,
)


def sanitize_filename(filename: str) -> str:
    model = models.create_groq_chat_model(temperature=1)
    rs = model.invoke(input=[
        SystemMessage(content='''
            You are a filename sanitization tool.
            The goal is to convert user-provided filenames into a legal format containing only the following characters: lowercase letters (a-z), uppercase letters (A-Z), numbers (0-9), hyphens (-), underscores (_), and periods (.).
            Please note that any file path separators (such as forward slash "/", backslash "", etc.) should not be preserved and should be treated as unconvertible characters.
            The processing rules are as follows:
            1. Preserve Legal Characters: If a character in the original filename is already within [0-9a-zA-Z-._], it should remain unchanged.
            2. Attempt English Conversion: If the filename contains characters from other languages (e.g., Chinese, Japanese, Korean), an attempt should be made to convert them to their corresponding English letters or Pinyin. For Chinese characters, the most common Pinyin representation should be used.
            3. Fallback for Unconvertible Characters: If a character in the filename cannot be meaningfully converted to English letters or Pinyin, or if the conversion result is undesirable, or if it is a file path separator, it should be replaced with an underscore (_).
            4. Maintain Original Filename Structure: Efforts should be made to preserve the original structure and delimiters (e.g., hyphens or underscores separating words) of the filename after conversion.
            5. Conciseness: The generated legal filename should be as concise and clear as possible.
            Please note that the output should only be the final sanitized filename, without any explanations or additional text.
        '''),
        HumanMessage(content=filename),
    ])
    return rs.content
