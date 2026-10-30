# ContextBridge MCP

Production-minded Java MCP server that exposes engineering context through typed, inspectable and non-destructive tools.

[![CI](https://github.com/thillainirmal-tech/ContextBridge-MCP/actions/workflows/ci.yml/badge.svg)](https://github.com/thillainirmal-tech/ContextBridge-MCP/actions/workflows/ci.yml)
![MCP](https://img.shields.io/badge/MCP-Spring_AI-2764FF)
![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)

## Tools

| Tool | Safety | Purpose |
|---|---|---|
| `listServices` | Read-only | Discover services and ownership |
| `serviceHealth` | Read-only | Inspect synthetic health metadata |
| `findRunbook` | Read-only | Retrieve incident guidance |
| `previewDeployment` | Dry-run | Plan deployments without mutation |
| `estimateMonthlyCost` | Pure | Calculate illustrative compute cost |

The server uses the Spring AI MCP WebMVC starter and publishes real MCP tool callbacks. Remote MCP endpoints require `Authorization: Bearer <CONTEXTBRIDGE_API_KEY>`; health and the inspection landing page remain public. All bundled data is synthetic.

## Run

Set a random `CONTEXTBRIDGE_API_KEY` of at least 24 characters, then run `mvn spring-boot:run`.

Open http://localhost:8084 for the inspection dashboard. Configure an MCP client against the WebMVC MCP transport exposed by Spring AI.

## Safety design

- no tool mutates infrastructure
- production deployment previews always require human approval
- unknown resources return structured errors
- tool descriptions explicitly state side-effect behavior
- synthetic catalogue data prevents accidental disclosure

## Verify

`mvn verify`

## Roadmap

- OAuth 2.1 resource-server mode for multi-tenant deployments
- MCP resources for runbooks and service schemas
- per-tool authorization policies
- OpenTelemetry tool-call traces
- signed audit events and rate limits

Apache-2.0. Never commit credentials or real internal service data.
