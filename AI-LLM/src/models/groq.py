import os
import httpx
from langchain_groq import ChatGroq
from groq import Groq
from utils import config

def create_groq_chat_model(model="llama3-70b-8192", temperature: float = 0):
    groq_model = ChatGroq(
        model=model,
        temperature=temperature,
        # more options can be added here
        api_key=config.get_env_variable_or_prompt(GroqConfig.ENV_GROQ_API_KEY),
        http_client=httpx.Client(proxy=os.getenv(GroqConfig.ENV_GROQ_PROXY)),
    )
    return groq_model

def create_groq_model():
    groq = Groq (
        http_client=httpx.Client(proxy=os.getenv(GroqConfig.ENV_GROQ_PROXY)),
    )
    return groq

class GroqConfig:
    ENV_GROQ_API_KEY = "GROQ_API_KEY"
    ENV_GROQ_PROXY = "GROQ_PROXY"

# Uncomment and enter values below for easier execution.
# set_env_variable_if_missing(ENV_GROQ_API_KEY, '')
# set_env_variable_if_missing(ENV_HTTP_PROXY, '')
