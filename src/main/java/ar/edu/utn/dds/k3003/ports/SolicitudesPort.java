package ar.edu.utn.dds.k3003.ports;

import ar.edu.utn.dds.k3003.facades.dtos.SolicitudDTO;
import java.util.List;

public interface SolicitudesPort {
    List<SolicitudDTO> listarTodas();
}
