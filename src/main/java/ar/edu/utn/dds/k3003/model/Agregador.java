package ar.edu.utn.dds.k3003.model;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.dtos.ConsensosEnum;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.model.consenso.AggregationStrategy;
import ar.edu.utn.dds.k3003.model.consenso.StrategyRegistry;
import ar.edu.utn.dds.k3003.model.consenso.ConsensoTipo;
import lombok.Data;

@Data
public class Agregador {
    private List<Fuente> lista_fuentes = new ArrayList<>();
    private Map<String, FachadaFuente> fachadaFuentes = new HashMap<>();
    private Map<String, ConsensoTipo> tipoConsensoXColeccion = new HashMap<>();
    private StrategyRegistry strategyRegistry;

    public Map<String, ConsensoTipo> getTipoConsensoXColeccion() { return tipoConsensoXColeccion; }

    public Fuente agregarFuente(Fuente newFuente) {
        lista_fuentes.add(newFuente);
        return newFuente;
    }

    public void configurarConsenso(ConsensoTipo tipo, String nombreColeccion) {
        tipoConsensoXColeccion.put(nombreColeccion, tipo);
    }

    public void configurarConsenso(ConsensosEnum ext, String nombreColeccion) {
        configurarConsenso(ConsensoTipo.from(ext), nombreColeccion);
    }
    private List<Hecho> obtenerHechosDeTodasLasFuentes(String nombreColeccion) {
        List<Hecho> hechos = new ArrayList<>();
        for (Fuente fuente : lista_fuentes) {
            var fachada = fachadaFuentes.get(fuente.getId());
            if (fachada == null) continue;
            try {
                List<HechoDTO> hechosDTO = fachada.buscarHechosXColeccion(nombreColeccion);
                hechos.addAll(hechosDTO.stream().map(dto -> {
                    Hecho h = new Hecho(dto.titulo(), dto.id(), dto.nombreColeccion());
                    h.setOrigen(fuente.getId());
                    return h;
                }).collect(Collectors.toList()));
            } catch (Exception ignored) {
            }
        }
        return hechos;
    }


    public List<Hecho> obtenerHechosPorColeccion(String nombreColeccion) {
        if (!tipoConsensoXColeccion.containsKey(nombreColeccion))
            return Collections.emptyList();

        ConsensoTipo estrategia = tipoConsensoXColeccion.get(nombreColeccion);
        List<Hecho> hechosCrudos = obtenerHechosDeTodasLasFuentes(nombreColeccion);

        AggregationStrategy s = strategyRegistry.get(estrategia);
        return s.aplicar(hechosCrudos);
    }

    public void agregarFachadaAFuente(String fuenteId, FachadaFuente fuente) {
        Fuente existe_Fuente = lista_fuentes.stream()
                .filter(f -> f.getId().equals(fuenteId))
                .findAny()
                .orElse(null);

        if (existe_Fuente == null)
            throw new NoSuchElementException("No se encontro la fuente");

        fachadaFuentes.put(fuenteId, fuente);
        existe_Fuente.setFachadaFuente(fuente);
    }

}