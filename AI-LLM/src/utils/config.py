import os
import getpass

def set_env_variable_if_missing(key, value):
    if not os.getenv(key) and value:
        os.environ[key] = value

def get_env_variable_or_prompt(key):
    value = os.getenv(key)
    while not value:
        value = getpass.getpass(f"Please enter a value for {key}: ").strip()
        if value:
            os.environ[key] = value
    return value
