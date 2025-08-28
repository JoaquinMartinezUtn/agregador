## Solicitudes – aceptar censura
```mermaid
flowchart TD
SA[Inicio] --> SB[PATCH estado=aceptada]
SB --> SC[Validar transición]
SC -->|inválida| SX[Error]
SC -->|válida| SD[Actualizar estado/descr]
SD --> SE[Censurar Hecho en Fuente]
SE --> SF[Registrar resultado]
SF --> SG[Retornar]
