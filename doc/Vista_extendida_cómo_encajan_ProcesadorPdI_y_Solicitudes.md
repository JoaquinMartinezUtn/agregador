## Vista extendida (cómo encajan ProcesadorPdI y Solicitudes)
```mermaid
flowchart LR
  C[Contribuyente] --> FD[Fuente Dinamica]
  FD -->|envia PDI para validar| P[Procesador PDI]
  P -->|consulta estado de Hecho| S[Solicitudes]
  S -->|OK o NO| P
  P -->|resultado de procesamiento| FD
  S -->|si solicitud aceptada -> censurar| FE[Fuente Estatica]
  S -->|si solicitud aceptada -> censurar| FD
  S -->|si solicitud aceptada -> censurar| FP[Fuente Proxy]
  AG[Agregador] -->|consulta hechos solo no censurados| FE
  AG -->|consulta hechos| FD
  AG -->|consulta hechos| FP
