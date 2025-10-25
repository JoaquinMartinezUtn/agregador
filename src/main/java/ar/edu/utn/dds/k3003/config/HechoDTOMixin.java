// src/main/java/ar/edu/utn/dds/k3003/config/HechoDTOMixin.java
package ar.edu.utn.dds.k3003.config;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class HechoDTOMixin {
    @JsonProperty("nombreColeccion")
    @JsonAlias({"nombre_coleccion"})
    public abstract String nombreColeccion();
}
