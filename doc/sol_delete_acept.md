## Solicitud de eliminación aceptada
```mermaid
sequenceDiagram
participant U as Usuario
participant S as Solicitudes
participant F as Fuente (E/D/Proxy)


U->>S: POST /solicitudes (hechoId, texto>=500)
S-->>U: solicitud creada (pendiente)
U->>S: PATCH /solicitudes/{id} estado=aceptada
S->>F: censurarHecho(hechoId)
F-->>S: OK (Hecho ya no se expone)
S-->>U: solicitud aceptada + censura aplicada
