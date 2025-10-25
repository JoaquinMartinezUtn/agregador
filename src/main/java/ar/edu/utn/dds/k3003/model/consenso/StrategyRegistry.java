// src/main/java/ar/edu/utn/dds/k3003/model/consenso/StrategyRegistry.java
package ar.edu.utn.dds.k3003.model.consenso;

import ar.edu.utn.dds.k3003.facades.dtos.ConsensosEnum;
import ar.edu.utn.dds.k3003.ports.SolicitudesPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class StrategyRegistry {
    private static final Logger log = LoggerFactory.getLogger(StrategyRegistry.class);

    private final Map<ConsensoTipo, AggregationStrategy> byLocal = new EnumMap<>(ConsensoTipo.class);
    private final Map<String, AggregationStrategy> byKey = new HashMap<>();

    public StrategyRegistry() {
        log.info("🔧 Inicializando StrategyRegistry (básico)");
        byLocal.put(ConsensoTipo.TODOS, new TodosStrategy());
        byLocal.put(ConsensoTipo.AL_MENOS_2, new AlMenosDosStrategy());
        byLocal.forEach((k,v) -> byKey.put(k.name(), v));
        log.info("✅ Registradas básicas: {}", byKey.keySet());
    }

    @Autowired
    public void setSolicitudesPort(SolicitudesPort solicitudesPort) {
        log.info("🔌 setSolicitudesPort llamado. null? {}", solicitudesPort == null);
        try {
            var estricto = new EstrictoStrategy(solicitudesPort);
            byLocal.put(ConsensoTipo.ESTRICTO, estricto);
            byKey.put("ESTRICTO", estricto);
            log.info("✅ ESTRICTO registrado. keys={}", byKey.keySet());
        } catch (Exception e) {
            log.error("❌ Error registrando ESTRICTO", e);
        }
    }

    // Compat externo → interno
    public AggregationStrategy get(ConsensosEnum ext) {
        log.info("🧭 get(ConsensosEnum={})", ext);
        return get(ConsensoTipo.from(ext));
    }

    public AggregationStrategy get(ConsensoTipo tipo) {
        log.info("🧭 get(ConsensoTipo={})", tipo);
        var s = byLocal.get(tipo);
        if (s == null) {
            log.error("❌ Estrategia no soportada (local): {}", tipo);
            throw new IllegalArgumentException("Estrategia no soportada: " + tipo);
        }
        log.info("✅ Devolviendo {}", s.getClass().getSimpleName());
        return s;
    }

    public AggregationStrategy get(String key) {
        log.info("🧭 get(key={})", key);
        var s = byKey.get(key == null ? null : key.toUpperCase());
        if (s == null) {
            log.error("❌ Estrategia no soportada (key): {}", key);
            throw new IllegalArgumentException("Estrategia no soportada: " + key);
        }
        log.info("✅ Devolviendo {}", s.getClass().getSimpleName());
        return s;
    }
}
