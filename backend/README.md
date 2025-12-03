# Backend

API REST avec Quarkus (Java 21).

## Lancer

```bash
mvn quarkus:dev
```

L'API est accessible sur http://localhost:8080

## Endpoints

- `GET /hello` - Retourne un message de salutation
- `GET /hello?name=John` - Retourne un message personnalisé

## Tests

```bash
mvn test
```
