package ar.edu.utn.dds.k3003.model.consenso;

import ar.edu.utn.dds.k3003.model.Hecho;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/** Devuelve todos, deduplicados por título (identidad definida por el dominio). */
public class TodosStrategy implements AggregationStrategy {
    @Override
    public List<Hecho> aplicar(List<Hecho> hechos) {
        Map<String, Hecho> unicos = hechos.stream()
                .collect(Collectors.toMap(Hecho::getTitulo, Function.identity(), (a, b) -> a));
        return new ArrayList<>(unicos.values());
    }
}
