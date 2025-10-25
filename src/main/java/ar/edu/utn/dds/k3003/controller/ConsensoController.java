// src/main/java/ar/edu/utn/dds/k3003/controller/ConsensoController.java
package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.FachadaAgregador;
import ar.edu.utn.dds.k3003.facades.dtos.ConsensosEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/consenso")
public class ConsensoController {

    private static final Logger log = LoggerFactory.getLogger(ConsensoController.class);
    private final FachadaAgregador fachadaAgregador;

    public ConsensoController(FachadaAgregador fachadaAgregador) {
        this.fachadaAgregador = fachadaAgregador;
    }

    @PatchMapping
    public ResponseEntity<?> configurarConsenso(@RequestBody Map<String, String> body) {
        String tipo = body == null ? "" : body.getOrDefault("tipo", "");
        String coleccion = body == null ? "" : body.getOrDefault("coleccion", "");

        log.info("🛠️ PATCH /consenso tipo='{}' coleccion='{}'", tipo, coleccion);

        try {
            String up = tipo == null ? "" : tipo.trim().toUpperCase();

            // Caso 1: nuestro “ESTRICTO” (no existe en el enum externo)
            if ("ESTRICTO".equals(up)) {
                if (fachadaAgregador instanceof Fachada f) {
                    f.setConsensoStrategyString(up, coleccion);
                    log.info("✅ Consenso ESTRICTO seteado (interno) coleccion={}", coleccion);
                    return ResponseEntity.noContent().build();
                } else {
                    log.error("❌ fachadaAgregador no es instancia de Fachada concreta");
                    Map<String, Object> err = new HashMap<>();
                    err.put("error", "configuracion");
                    err.put("detalle", "No es posible setear ESTRICTO en esta implementación");
                    return ResponseEntity.status(500).body(err);
                }
            }

            // Caso 2: Sólo aceptar explícitamente los que sí existen en el enum externo
            if ("TODOS".equals(up) || "AL_MENOS_2".equals(up)) {
                ConsensosEnum consenso = ConsensosEnum.valueOf(up);
                fachadaAgregador.setConsensoStrategy(consenso, coleccion);
                log.info("✅ Consenso seteado (externo) coleccion={} tipo={}", coleccion, consenso);
                return ResponseEntity.noContent().build();
            }

            // Caso 3: cualquier otro valor => 400
            log.error("❌ Tipo de consenso no soportado: {}", tipo);
            Map<String, Object> bad = new HashMap<>();
            bad.put("error", "tipo no soportado");
            bad.put("detalle", "Usá: TODOS, AL_MENOS_2 o ESTRICTO");
            return ResponseEntity.badRequest().body(bad);

        } catch (Exception ex) {
            log.error("🌋 Error inesperado en PATCH /consenso", ex);
            Map<String, Object> err = new HashMap<>();
            err.put("error", "error interno");
            err.put("detalle", ex.getMessage());
            return ResponseEntity.status(500).body(err);
        }
    }

    @GetMapping("/ping")
    public String ping() {
        log.info("👋 ping ConsensoController OK");
        return "pong";
    }
}
