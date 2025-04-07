# AI-LLM-Playground

This project provides examples of using AI large language models (LLMs). 

To run the examples, ensure that you configure the `src/config.py` file as needed .

## Environment Setup

This project uses Miniconda for environment setup during development. To initialize the environment, run the following commands:

```bash
conda create --name ai-llm-playground python=3
conda activate ai-llm-playground

conda install langchain -c conda-forge
pip install -qU "langchain[groq]"
```
### Package Versions

The following are the package versions used during development for this project:

- python: `3.13.2`
- langchain-core: `0.3.51`
- langchain-groq: `0.3.2`
