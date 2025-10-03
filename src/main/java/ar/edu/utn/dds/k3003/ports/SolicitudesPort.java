package ar.edu.utn.dds.k3003.ports;

import ar.edu.utn.dds.k3003.facades.dtos.SolicitudDTO;
import java.util.List;

/** Puerto para consultar el módulo de Solicitudes (evita acoplar a HTTP desde el dominio). */
public interface SolicitudesPort {
    List<SolicitudDTO> listarTodas();
}
