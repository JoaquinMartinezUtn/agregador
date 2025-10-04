package ar.edu.utn.dds.k3003.model.visibilidad;

import ar.edu.utn.dds.k3003.facades.dtos.SolicitudDTO;
import ar.edu.utn.dds.k3003.model.Hecho;
import ar.edu.utn.dds.k3003.ports.SolicitudesPort;

import java.util.*;
import java.util.stream.Collectors;

public class SinSolicitudesPolicy implements VisibilityPolicy {

    private final SolicitudesPort solicitudes;

    public SinSolicitudesPolicy(SolicitudesPort solicitudes) {
        this.solicitudes = solicitudes;
    }

    @Override
    public List<Hecho> filtrar(List<Hecho> hechos) {
        if (hechos.isEmpty()) return hechos;
        Map<String, String> idATitulo = hechos.stream()
                .filter(h -> h.getId() != null && !h.getId().trim().isEmpty())
                .collect(Collectors.toMap(Hecho::getId, Hecho::getTitulo, (a, b) -> a));
        if (idATitulo.isEmpty()) return hechos;
        Set<String> idsPresentes = idATitulo.keySet();

        List<SolicitudDTO> solicitudesAll = solicitudes.listarTodas();
        Set<String> titulosBloqueados = solicitudesAll.stream()
                .map(SolicitudDTO::hechoId)
                .filter(Objects::nonNull)
                .filter(idsPresentes::contains)
                .map(idATitulo::get)
                .collect(Collectors.toSet());

        if (titulosBloqueados.isEmpty()) return hechos;

        return hechos.stream()
                .filter(h -> !titulosBloqueados.contains(h.getTitulo()))
                .collect(Collectors.toList());
    }
}
