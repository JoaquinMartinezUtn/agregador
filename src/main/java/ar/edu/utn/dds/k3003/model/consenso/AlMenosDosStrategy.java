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

        // Títulos que aparecen en al menos 2 orígenes distintos
        Set<String> repetidos = hechos.stream()
                .collect(Collectors.groupingBy(Hecho::getTitulo,
                        Collectors.mapping(Hecho::getOrigen, Collectors.toSet())))
                .entrySet().stream()
                .filter(e -> e.getValue().size() >= 2)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        // Dedup por título, quedándome con un ejemplar de cada título repetido
        return hechos.stream()
                .filter(h -> repetidos.contains(h.getTitulo()))
                .collect(Collectors.toMap(Hecho::getTitulo, Function.identity(), (a, b) -> a))
                .values().stream().collect(Collectors.toList());
    }
}
