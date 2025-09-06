CREATE TABLE IF NOT EXISTS fuente (
  id VARCHAR(255) PRIMARY KEY,
  nombre VARCHAR(255) NOT NULL,
  endpoint TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS coleccion (
  nombre VARCHAR(255) PRIMARY KEY,
  descripcion TEXT,
  fecha_modificacion TIMESTAMP
);

CREATE TABLE IF NOT EXISTS consenso_coleccion (
  nombre_coleccion VARCHAR(255) PRIMARY KEY,
  consenso VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS solicitud_eliminacion (
  id UUID PRIMARY KEY,
  nombre_coleccion VARCHAR(255) NOT NULL,
  titulo VARCHAR(512) NOT NULL,
  motivo TEXT,
  estado VARCHAR(32) NOT NULL DEFAULT 'PENDIENTE',
  creado_en TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_solicitud_coleccion_titulo
  ON solicitud_eliminacion (nombre_coleccion, titulo);



-- Tabla HECHO (coincide con los @Column de la entidad)
CREATE TABLE IF NOT EXISTS hecho (
  id               VARCHAR(255) PRIMARY KEY,
  nombre_coleccion VARCHAR(255) NOT NULL,
  titulo           VARCHAR(255) NOT NULL,
  descripcion      TEXT         NOT NULL,
  lugar            VARCHAR(255) NOT NULL,
  fecha            DATE         NOT NULL,
  fecha_carga      DATE         NOT NULL,
  origen           VARCHAR(255) NOT NULL,
  fuente_id        VARCHAR(255),

  CONSTRAINT fk_hecho_fuente
    FOREIGN KEY (fuente_id) REFERENCES fuente(id)
);

-- Índices útiles
CREATE INDEX IF NOT EXISTS idx_hecho_coleccion_titulo
  ON hecho (nombre_coleccion, titulo);
