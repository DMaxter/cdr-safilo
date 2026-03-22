FROM astral/uv:python3.12-bookworm-slim

WORKDIR /app

COPY pyproject.toml uv.lock ./
RUN uv sync --no-install-project

COPY mockserver.py pdftest.pdf ./

EXPOSE 9091

CMD ["uv", "run", "uvicorn", "mockserver:app", "--host", "0.0.0.0", "--port", "9091"]
