## Agregador – obtener hechos de una colección
```mermaid
flowchart TD
  A[Inicio] --> B[Leer nombre de coleccion]
  B --> C[Obtener fuentes registradas]
  C -->|0 fuentes| Z[Retornar lista vacia]
  C -->|>= 1| D[Llamar a cada fuente - listar hechos de coleccion]
  D --> E[Unificar resultados]
  E --> F[Normalizar y deduplicar por nombre]
  F --> G[Contar apariciones por nombre]
  G --> H[Leer tipo de consenso para la coleccion]
  H --> I{tipo == todos?}
  I -->|Si| J[Filtrar: todos]
  I -->|No| K{n_fuentes == 1?}
  K -->|Si| J[Filtrar: todos - excepcion]
  K -->|No| L[Filtrar: apariciones >= 2]
  J --> M[Ordenar y retornar]
  L --> M[Ordenar y retornar]
  Z --> M
