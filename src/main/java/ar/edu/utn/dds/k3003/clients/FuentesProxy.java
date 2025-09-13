package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.facades.dtos.ColeccionDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import ar.edu.utn.dds.k3003.facades.dtos.PdIDTO;
import ar.edu.utn.dds.k3003.facades.FachadaProcesadorPdI;

public class FuentesProxy implements FachadaFuente {

    private final FuentesRetrofitClient service;
    private FachadaProcesadorPdI procesadorPdI;

    public FuentesProxy(String baseUrl, ObjectMapper objectMapper) {
        var logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ensureSlash(baseUrl))
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .client(client)
                .build();

        this.service = retrofit.create(FuentesRetrofitClient.class);
    }

    private static String ensureSlash(String url) { return url.endsWith("/") ? url : url + "/"; }

    @SneakyThrows
    @Override
    public List<HechoDTO> buscarHechosXColeccion(String nombreColeccion) {
        var r = service.hechosPorColeccionPlural(nombreColeccion).execute();
        if (r.isSuccessful() && r.body() != null) return r.body();
        if (r.code() == 204 || r.code() == 404) {
            // Fallback singular
            var r2 = service.hechosPorColeccionSingular(nombreColeccion).execute();
            if (r2.isSuccessful() && r2.body() != null) return r2.body();
            if (r2.code() == 204 || r2.code() == 404) return Collections.emptyList();
            throw new RuntimeException("Error fuente (singular) HTTP " + r2.code());
        }
        throw new RuntimeException("Error fuente (plural) HTTP " + r.code());
    }

    @SneakyThrows
    @Override
    public List<ColeccionDTO> colecciones() {
        var r = service.colecciones().execute();
        if (r.isSuccessful() && r.body() != null) return r.body();
        if (r.code() == 204 || r.code() == 404) return Collections.emptyList();
        throw new RuntimeException("Error fuente HTTP " + r.code());
    }

    @Override
    public void setProcesadorPdI(FachadaProcesadorPdI fachadaProcesadorPdI) {
        this.procesadorPdI = fachadaProcesadorPdI; // no-op por ahora
    }

    @SneakyThrows
    @Override
    public HechoDTO buscarHechoXId(String id) throws NoSuchElementException {
        var r = service.hecho(id).execute();
        if (r.isSuccessful() && r.body() != null) return r.body();
        if (r.code() == 404) throw new NoSuchElementException("Hecho no encontrado: " + id);
        throw new RuntimeException("Error fuente HTTP " + r.code());
    }

    @Override
    public PdIDTO agregar(PdIDTO dto) {
        throw new UnsupportedOperationException("agregar(PdIDTO) no implementado en FuentesProxy aún");
    }

    @Override
    public HechoDTO agregar(HechoDTO dto) {
        throw new UnsupportedOperationException("agregar(HechoDTO) no implementado en FuentesProxy aún");
    }


    @SneakyThrows
    @Override
    public ColeccionDTO agregar(ColeccionDTO dto) {
        var r = service.crearColeccion(dto).execute();
        if (r.isSuccessful() && r.body() != null) return r.body();
        throw new RuntimeException("Error creando colección HTTP " + r.code());
    }

    @SneakyThrows
    @Override
    public ColeccionDTO buscarColeccionXId(String id) throws NoSuchElementException {
        var r = service.coleccion(id).execute();
        if (r.isSuccessful() && r.body() != null) return r.body();
        if (r.code() == 404) throw new NoSuchElementException("Colección no encontrada: " + id);
        throw new RuntimeException("Error fuente HTTP " + r.code());
    }

    public HechoDTO marcarBorrado(String id) {
        try {
            var r = service.patchHecho(id, java.util.Collections.singletonMap("estado", "borrado")).execute();
            if (r.isSuccessful() && r.body() != null) return r.body();
            throw new RuntimeException("Error parcheando hecho HTTP " + r.code());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
