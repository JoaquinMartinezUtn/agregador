// src/main/java/ar/edu/utn/dds/k3003/controller/ColeccionController.java
package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.facades.FachadaAgregador;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/coleccion")
public class ColeccionController {

    private final FachadaAgregador fachadaAgregador;
    private final MeterRegistry meter;

    public ColeccionController(FachadaAgregador fachadaAgregador, MeterRegistry meter) {
        this.fachadaAgregador = fachadaAgregador;
        this.meter = meter;
    }

    @GetMapping("/{nombre}/hechos")
    public ResponseEntity<List<HechoDTO>> listarHechosPorColeccion(@PathVariable String nombre) {
        var sample = Timer.start(meter);
        try {
            var data = fachadaAgregador.hechos(nombre);
            meter.counter("agregador.hechos.requests",
                    "coleccion", nombre,
                    "outcome", "ok").increment();
            return ResponseEntity.ok(data);
        } catch (NoSuchElementException e) {
            meter.counter("agregador.hechos.requests",
                    "coleccion", nombre,
                    "outcome", "not_found").increment();
            throw e;
        } catch (RuntimeException e) {
            meter.counter("agregador.hechos.requests",
                    "coleccion", nombre,
                    "outcome", "error").increment();
            throw e;
        } finally {
            sample.stop(meter.timer("agregador.hechos.latency",
                    "coleccion", nombre));
        }
    }
}
