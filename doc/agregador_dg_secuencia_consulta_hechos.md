## Solicitudes – aceptar censura
```mermaid
sequenceDiagram
participant C as Controller
participant FA as Fachada
participant AG as Agregador
participant FR as FuenteRepository
participant F1 as Fuente A (FachadaFuente)
participant F2 as Fuente B (FachadaFuente)


C->>FA: hechos(nombreColeccion)
FA->>FR: findAll()
FR-->>FA: List<Fuente>
FA->>AG: setLista_fuentes(...)
FA->>AG: obtenerHechosPorColeccion(nombreColeccion)
AG->>F1: listarHechos(coleccion)
F1-->>AG: Hechos A
AG->>F2: listarHechos(coleccion)
F2-->>AG: Hechos B
AG->>AG: unificar / normalizar / deduplicar / contar
AG->>AG: aplicar consenso (estrategia por colección)
AG-->>FA: List<Hecho>
FA-->>C: List<HechoDTO>
