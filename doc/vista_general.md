## Arquitectura lógica y relaciones entre módulos de MetaMapa
```mermaid
flowchart LR
subgraph Publico
V[Visualizador]
C[Contribuyente]
end


subgraph Agregador
AG[(Servicio Agregador)]
end


subgraph Fuentes
FE[(Fuente Estática)]
FD[(Fuente Dinámica)]
FP[(Fuente Proxy)]
end


subgraph Moderacion
S[(Solicitudes)]
end


subgraph Procesamiento
P[(ProcesadorPdI)]
end


V -->|consulta colecciones/hechos| AG
C -->|sube hechos| FD
AG -->|consulta hechos| FE
AG -->|consulta hechos| FD
AG -->|consulta hechos| FP


FD -->|agrega PdI si P valida| P
P -->|verifica Hecho activo| S
S -->|censura Hecho| FE
S -->|censura Hecho| FD
S -->|censura Hecho| FP
