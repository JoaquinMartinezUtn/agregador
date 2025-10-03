package ar.edu.utn.dds.k3003.model.consenso;

import ar.edu.utn.dds.k3003.facades.dtos.ConsensosEnum;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

/** Resuelve ConsensosEnum → AggregationStrategy (evita switch en el Agregador). */
@Component
public class StrategyRegistry {
    private final Map<ConsensosEnum, AggregationStrategy> map = new EnumMap<>(ConsensosEnum.class);

    public StrategyRegistry() {
        map.put(ConsensosEnum.TODOS, new TodosStrategy());
        map.put(ConsensosEnum.AL_MENOS_2, new AlMenosDosStrategy());
    }

    public AggregationStrategy get(ConsensosEnum tipo) {
        AggregationStrategy s = map.get(tipo);
        if (s == null) throw new IllegalArgumentException("Estrategia no soportada: " + tipo);
        return s;
    }
}
