### Como Executar

É necessário ter o [uv](https://docs.astral.sh/uv/getting-started/installation/) instalado.

**Executar com uvicorn**

```bash
uv run uvicorn mockserver:app --reload
```

**Executar a classe**

```bash
uv run python mockserver.py
```

**Configuração**

A propriedade `quarkus.rest-client.fema-expeditions.url` tem de ser definida para: `http://localhost:9091/FemaMock`
