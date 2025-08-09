# platinumcoin-router — gateway simples (logs via ELK)

**Objetivo:** receber requisições do cliente, **gerar/propagar `x-correlation-id`** e encaminhar ao serviço de back-end (**platinumcoin-observability-log**) enquanto envia **logs JSON** para o Logstash (ELK).

**Projeto Original (logs):** [https://github.com/filiperibolli/platinumcoin-observability-logs](https://github.com/filiperibolli/platinumcoin-observability-logs)

---

## Stack mínima

* **Java 17** + **Spring Boot 3**
* **Logback** com `logstash-logback-encoder` (logs **JSON**)
* **ELK**: Elasticsearch, Logstash (input TCP 5000), Kibana
* **Correlação**: header **`x-correlation-id`** (entra no MDC e vai para o log)

---

## Execução rápida (Windows)

### Via Docker Compose (recomendado)

Adicione este serviço ao seu `docker-compose.yml` (junto de ELK):

```yaml
a:
  image: ribolli1/platinumcoin-router:0.1.1
  container_name: platinumcoin-router
  environment:
    LOGSTASH_HOST: logstash
    LOGSTASH_PORT: 5000
    B_BASE_URL: http://host.docker.internal:8082  # serviço de backend rodando fora do Docker
  depends_on: [logstash]
  ports: ["8081:8081"]
```

> No **Windows**, `host.docker.internal` resolve para a sua máquina.

### Teste

```bash
# requisição simples
curl -i http://localhost:8081/orders/123

# com correlação explícita
curl -i -H "x-correlation-id: demo-123" http://localhost:8081/orders/123
```

---

## Variáveis de ambiente

* `LOGSTASH_HOST` (padrão: `logstash`)
* `LOGSTASH_PORT` (padrão: `5000`)
* `B_BASE_URL` (padrão recomendado no Windows: `http://host.docker.internal:8082`)
* `SERVER_PORT` (opcional, padrão `8081`)

---

## Endpoints

* `GET /orders/{id}` → encaminha ao back-end (platinumcoin-observability-log) e retorna a resposta.
* `GET /actuator/health` → health check (se `spring-boot-starter-actuator` estiver presente).

---

## Observabilidade (Kibana)

**Data View:** `app-logs-*` (`@timestamp`). Exemplos (KQL):

* `service.name: "platinumcoin-router"`
* `correlationId: "demo-123"`
* `service.name: "platinumcoin-router" and level: "ERROR"`

Campos principais no log JSON: `@timestamp`, `level`, `message`, `service.name`, `service.environment`, `correlationId`, `http.method?`, `http.route?`, `http.status_code?`, `error.stack?`.

---

## Problemas comuns

* **Conexão com B falhou**: verifique `B_BASE_URL` e se o back-end está ouvindo em `0.0.0.0:8082` (firewall liberado).
* **Sem logs no Kibana**: confirme `LOGSTASH_HOST/PORT` e se o Logstash está ouvindo em `5000`.
* **`x-correlation-id` ausente**: o router gera automaticamente se não vier do cliente.

---

## Licença

Uso interno para estudo e validação de observabilidade (ELK).
