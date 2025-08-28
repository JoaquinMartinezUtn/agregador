## ProcesadorPdI – procesar PdI
```mermaid
flowchart TD
PA[Inicio] --> PB[Recibir PDI: hecho_id + contenido]
PB --> PC{Hecho activo? consultar a Solicitudes}
PC -->|No| PX[Rechazar procesamiento]
PC -->|Si| PD{PDI ya procesada?}
PD -->|Si| PY[Retornar resultado cacheado]
PD -->|No| PE[Etiquetar y guardar resultado]
PE --> PZ[Retornar OK]
