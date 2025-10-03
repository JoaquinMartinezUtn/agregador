package ar.edu.utn.dds.k3003.model.visibilidad;

import ar.edu.utn.dds.k3003.model.Hecho;
import java.util.List;

/** Policy (Specification/Filter) para reglas de visibilidad post-agregación. */
public interface VisibilityPolicy {
    List<Hecho> filtrar(List<Hecho> hechos);
}
