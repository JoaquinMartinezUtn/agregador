## Diagrama de Estados (configuración de consenso por colección)
```mermaid
stateDiagram-v2
[*] --> SinConfig
SinConfig --> Configurada: configurarConsenso(coleccion)
Configurada --> Configurada: reconfigurarConsenso(coleccion)
Configurada --> [*]
