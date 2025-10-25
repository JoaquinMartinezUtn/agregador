// src/main/java/ar/edu/utn/dds/k3003/model/consenso/EstrictoStrategy.java
package ar.edu.utn.dds.k3003.model.consenso;

import ar.edu.utn.dds.k3003.facades.dtos.SolicitudDTO;
import ar.edu.utn.dds.k3003.model.Hecho;
import ar.edu.utn.dds.k3003.ports.SolicitudesPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class EstrictoStrategy implements AggregationStrategy {
    private static final Logger log = LoggerFactory.getLogger(EstrictoStrategy.class);

    private final SolicitudesPort solicitudes;

    public EstrictoStrategy(SolicitudesPort solicitudes) {
        this.solicitudes = solicitudes;
        log.info("🔧 EstrictoStrategy creado. solicitudesPort != null? {}", this.solicitudes != null);
    }

    @Override
    public List<Hecho> aplicar(List<Hecho> hechos) {
        log.info("▶️ EstrictoStrategy.aplicar(hechos.size={})", (hechos == null ? "null" : hechos.size()));
        if (hechos == null || hechos.isEmpty()) return hechos;

        try {
            Map<String,String> idATitulo = hechos.stream()
                    .filter(h -> {
                        boolean keep = h != null && h.getId() != null && !h.getId().trim().isEmpty();
                        if (!keep) log.debug("⏭️ Hecho sin ID (se omite): {}", h);
                        return keep;
                    })
                    .collect(Collectors.toMap(Hecho::getId, Hecho::getTitulo, (a,b)->a));

            log.info("🧮 idATitulo.size={}", idATitulo.size());
            if (idATitulo.isEmpty()) return hechos;

            Set<String> idsPresentes = idATitulo.keySet();

            if (solicitudes == null) {
                log.error("❌ solicitudesPort es NULL dentro de EstrictoStrategy");
                throw new IllegalStateException("SolicitudesPort no inicializado");
            }

            List<SolicitudDTO> solicitudesAll = solicitudes.listarTodas();
            log.info("📬 solicitudesAll.size={}", (solicitudesAll == null ? 0 : solicitudesAll.size()));
            if (solicitudesAll == null) solicitudesAll = List.of();

            Set<String> titulosBloqueados = solicitudesAll.stream()
                    .map(SolicitudDTO::hechoId)
                    .peek(id -> log.debug("🔎 solicitud.hechoId={}", id))
                    .filter(Objects::nonNull)
                    .filter(idsPresentes::contains)
                    .map(idATitulo::get)
                    .collect(Collectors.toSet());

            log.info("🚫 titulosBloqueados.size={}", titulosBloqueados.size());
            if (titulosBloqueados.isEmpty()) return hechos;

            List<Hecho> filtrados = hechos.stream()
                    .filter(h -> !titulosBloqueados.contains(h.getTitulo()))
                    .collect(Collectors.toList());

            log.info("✅ EstrictoStrategy OK. in={}, out={}", hechos.size(), filtrados.size());
            return filtrados;
        } catch (Exception ex) {
            log.error("❌ Error en EstrictoStrategy.aplicar", ex);
            throw ex;
        }
    }
}
