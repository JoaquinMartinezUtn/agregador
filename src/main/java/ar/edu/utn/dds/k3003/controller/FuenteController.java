package ar.edu.utn.dds.k3003.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ar.edu.utn.dds.k3003.facades.FachadaAgregador;
import ar.edu.utn.dds.k3003.facades.dtos.FuenteDTO;

@RestController
@RequestMapping("/fuentes")
public class FuenteController {

    private final FachadaAgregador fachadaAgregador;

    public FuenteController(FachadaAgregador fachadaAgregador) {
        this.fachadaAgregador = fachadaAgregador;
    }

    @GetMapping
    public ResponseEntity<List<FuenteDTO>> fuentes() {
        return ResponseEntity.ok(fachadaAgregador.fuentes());
    }

    @PostMapping
    public ResponseEntity<FuenteDTO> agregarFuente(@RequestBody FuenteDTO fuenteDTO) {
        return ResponseEntity.ok(fachadaAgregador.agregar(fuenteDTO));
    }

    @DeleteMapping("/limpiar")
    public ResponseEntity<Void> limpiarFuentes() {
        if (fachadaAgregador instanceof ar.edu.utn.dds.k3003.app.Fachada fachadaConcreta) {
            fachadaConcreta.limpiarFuentes();
        } else {
            throw new UnsupportedOperationException("La fachada actual no soporta limpiarFuentes()");
        }
        return ResponseEntity.noContent().build();
    }
}
