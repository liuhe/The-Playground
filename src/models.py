import os
import config
import httpx
from langchain_groq import ChatGroq


def create_groq_chat_model(model="llama3-70b-8192", temperature: float = 0):
    groq_model = ChatGroq(
        model=model,
        temperature=temperature,
        # more options can be added here
        api_key=config.get_env_variable_or_prompt(config.ENV_GROQ_API_KEY),
        http_client=httpx.Client(proxy=os.getenv(config.ENV_GROQ_PROXY)),
    )
    return groq_model
