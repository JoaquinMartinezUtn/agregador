package ar.edu.utn.dds.k3003.config;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class SolicitudDTOMixin {
    @JsonProperty("hecho_id")
    @JsonAlias("hechoId")
    public abstract String hechoId();
}

