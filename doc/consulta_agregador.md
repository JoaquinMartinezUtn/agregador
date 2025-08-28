## Consulta pública (Agregador)
```mermaid
sequenceDiagram
participant U as Usuario
participant AG as Agregador
participant FE as Fuente Estática
participant FD as Fuente Dinámica
participant FP as Fuente Proxy


U->>AG: GET /coleccion/{nombre}/hechos
AG->>FE: listarHechos(coleccion)
AG->>FD: listarHechos(coleccion)
AG->>FP: listarHechos(coleccion)
FE-->>AG: [H1, H2]
FD-->>AG: [h1, H3]
FP-->>AG: [H4]
AG->>AG: unificar + dedup por nombre
AG->>AG: aplicar consenso (todos|almenos2, excepción 1 fuente)
AG-->>U: lista final ordenada
