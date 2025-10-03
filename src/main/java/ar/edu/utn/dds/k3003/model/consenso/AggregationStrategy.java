package ar.edu.utn.dds.k3003.model.consenso;

import ar.edu.utn.dds.k3003.model.Hecho;
import java.util.List;

public interface AggregationStrategy {
    List<Hecho> aplicar(List<Hecho> hechos);
}
