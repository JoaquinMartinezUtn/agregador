## Diagrama de Actividad (pipeline interno)
```mermaid
flowchart TD
A[Inicio] --> B[Leer nombre de coleccion]
B --> C[Obtener fuentes registradas]
C -->|0 fuentes| Z[Retornar lista vacia]
C -->|>= 1 fuente| D[Llamar a cada fuente: listar hechos]
D --> E[Unificar resultados]
E --> F[Normalizar y deduplicar por nombre/titulo]
F --> G[Contar apariciones por nombre]
G --> H[Leer tipo de consenso para la coleccion]
H --> I{tipo == TODOS}
I -->|Si| J[Filtrar: aparecen en todas]
I -->|No| K{n_fuentes == 1}
K -->|Si| J2["Filtrar: todos (excepcion 1 fuente)"]
K -->|No| L["Filtrar: apariciones >= 2 o MAYORIA"]
J --> M[Ordenar y retornar]
J2 --> M
L --> M
Z --> M
