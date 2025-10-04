package ar.edu.utn.dds.k3003.model.consenso;

import ar.edu.utn.dds.k3003.model.Hecho;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/** Mantiene títulos que aparecen en ≥2 fuentes distintas. */
public class AlMenosDosStrategy implements AggregationStrategy {
    @Override
    public List<Hecho> aplicar(List<Hecho> hechos) {
        if (hechos.isEmpty()) return List.of();

        Set<String> repetidos = hechos.stream()
                .collect(Collectors.groupingBy(Hecho::getTitulo,
                        Collectors.mapping(Hecho::getOrigen, Collectors.toSet())))
                .entrySet().stream()
                .filter(e -> e.getValue().size() >= 2)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        return hechos.stream()
                .filter(h -> repetidos.contains(h.getTitulo()))
                .collect(Collectors.toMap(Hecho::getTitulo, Function.identity(), (a, b) -> a))
                .values().stream().collect(Collectors.toList());
    }
}
