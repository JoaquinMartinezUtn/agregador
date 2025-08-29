## Solicitudes – aceptar censura
```mermaid
flowchart LR
C[REST Controller] -->|usa| FA[Fachada Agregador]
FA -->|coordina| AG[Agregador]
AG -->|lee/escribe| FR[(FuenteRepository)]
FR --> DB[(Base de Datos)]
AG -->|invoca| F1[FachadaFuente A]
AG -->|invoca| F2[FachadaFuente B]
AG -->|invoca| Fn[...]
