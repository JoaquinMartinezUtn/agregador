package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.facades.dtos.ColeccionDTO;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.Map;

public interface FuentesRetrofitClient {

    // Colecciones
    @GET("api/colecciones")
    Call<List<ColeccionDTO>> colecciones();

    @GET("api/coleccion/{nombre}")
    Call<ColeccionDTO> coleccion(@Path("nombre") String nombre);

    @POST("api/coleccion")
    Call<ColeccionDTO> crearColeccion(@Body ColeccionDTO dto);

    // Hechos por colección (contrato efectivo que viste funcionando)
    @GET("api/colecciones/{nombre}/hechos")
    Call<List<HechoDTO>> hechosPorColeccionPlural(@Path("nombre") String nombre);

    // Fallback por si alguna fuente usa singular
    @GET("api/coleccion/{nombre}/hechos")
    Call<List<HechoDTO>> hechosPorColeccionSingular(@Path("nombre") String nombre);

    // Hecho por id + patch de borrado
    @GET("api/hecho/{id}")
    Call<HechoDTO> hecho(@Path("id") String id);

    @PATCH("api/hecho/{id}")
    Call<HechoDTO> patchHecho(@Path("id") String id, @Body Map<String, String> body);
}
