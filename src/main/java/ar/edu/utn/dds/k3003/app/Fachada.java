package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.clients.FuentesProxy;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import ar.edu.utn.dds.k3003.facades.FachadaAgregador;
import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.dtos.ConsensosEnum;
import ar.edu.utn.dds.k3003.facades.dtos.FuenteDTO;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.model.Agregador;
import ar.edu.utn.dds.k3003.model.Fuente;
import ar.edu.utn.dds.k3003.model.Hecho;
import ar.edu.utn.dds.k3003.repository.JpaFuenteRepository;
import ar.edu.utn.dds.k3003.repository.FuenteRepository;
import org.springframework.beans.factory.annotation.Qualifier;

@Service
public class Fachada implements FachadaAgregador {

  private final Agregador agregador = new Agregador();
  private final FuenteRepository fuenteRepository;
  private final ObjectMapper objectMapper;

  public Fachada (@Qualifier("jpaFuenteRepository") FuenteRepository fuenteRepository, ObjectMapper objectMapper) {
    this.fuenteRepository = fuenteRepository;
    this.objectMapper = objectMapper;
  }

  @PostConstruct
  public void init() {
    var fuentes = fuenteRepository.findAll();
    agregador.setLista_fuentes(fuentes);
    fuentes.forEach(f ->
            agregador.agregarFachadaAFuente(
                    f.getId(),
                    new FuentesProxy(f.getEndpoint(), objectMapper)
            )
    );
  }

  @Override
  public FuenteDTO agregar(FuenteDTO fuenteDto) {
    String id = UUID.randomUUID().toString();
    Fuente fuente = new Fuente(id, fuenteDto.nombre(), fuenteDto.endpoint());
    fuenteRepository.save(fuente);

    agregador.agregarFuente(fuente);
    this.addFachadaFuentes(id, new FuentesProxy(fuenteDto.endpoint(), objectMapper));

    return convertirAFuenteDTO(fuente);
  }

  @Override
  public List<FuenteDTO> fuentes() {
    return fuenteRepository.findAll().stream().map(this::convertirAFuenteDTO).collect(Collectors.toList());
  }

  @Override
  public FuenteDTO buscarFuenteXId(String fuenteId) throws NoSuchElementException {
    return fuenteRepository.findById(fuenteId)
            .map(this::convertirAFuenteDTO)
            .orElseThrow(() -> new NoSuchElementException("Fuente no encontrada: " + fuenteId));
  }

  @Override
  public List<HechoDTO> hechos(String nombreColeccion) throws NoSuchElementException {
    agregador.setLista_fuentes(fuenteRepository.findAll());
    if (!agregador.getTipoConsensoXColeccion().containsKey(nombreColeccion)) {
      throw new NoSuchElementException("No hay consenso configurado para: " + nombreColeccion);
    }
    var hechosModelo = agregador.obtenerHechosPorColeccion(nombreColeccion);
    return hechosModelo.stream().map(this::convertirADTO).collect(java.util.stream.Collectors.toList());
  }

  @Override
  public void addFachadaFuentes(String fuenteId, FachadaFuente fuente) {
    agregador.agregarFachadaAFuente(fuenteId, fuente);
  }

  @Override
  public void setConsensoStrategy(ConsensosEnum tipoConsenso, String nombreColeccion)
          throws InvalidParameterException {
    agregador.configurarConsenso(tipoConsenso, nombreColeccion);
  }

  private HechoDTO convertirADTO(Hecho hecho) {
    return new HechoDTO(hecho.getId(), hecho.getColeccionNombre(), hecho.getTitulo());
  }

  private FuenteDTO convertirAFuenteDTO(Fuente fuente) {
    return new FuenteDTO(fuente.getId(), fuente.getNombre(), fuente.getEndpoint());
  }
}
