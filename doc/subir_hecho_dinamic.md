## Subida de Hecho dinámico y procesamiento de PdI
```mermaid
sequenceDiagram
participant C as Contribuyente
participant FD as Fuente Dinámica
participant P as ProcesadorPdI
participant S as Solicitudes


C->>FD: POST /hechos (texto + meta)
FD-->>C: Hecho creado (id)
C->>FD: POST /pdis (hechoId, contenido)
FD->>P: procesarPdI(hechoId, contenido)
P->>S: hechoActivo?(hechoId)
S-->>P: true/false
alt activo
P-->>FD: {etiquetas, procesado:true}
FD-->>C: PdI agregada
else censurado
P-->>FD: procesado:false
FD-->>C: rechazo
end
