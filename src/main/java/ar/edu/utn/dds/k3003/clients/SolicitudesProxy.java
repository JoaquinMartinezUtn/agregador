package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.facades.dtos.SolicitudDTO;
import ar.edu.utn.dds.k3003.ports.SolicitudesPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.GET;

import java.util.List;

@Component
public class SolicitudesProxy implements SolicitudesPort {

    interface Api {
        @GET("api/solicitudes")
        retrofit2.Call<List<SolicitudDTO>> listarTodas();
    }

    private final Api api;

    public SolicitudesProxy(
            @Value("${solicitudes.base-url}") String baseUrl,
            ObjectMapper objectMapper
    ) {
        var logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(logging).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl.endsWith("/") ? baseUrl : baseUrl + "/")
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .client(client)
                .build();
        this.api = retrofit.create(Api.class);
    }

    @Override
    @SneakyThrows
    public List<SolicitudDTO> listarTodas() {
        var r = api.listarTodas().execute();
        if (r.isSuccessful() && r.body() != null) return r.body();
        throw new RuntimeException("Error consultando Solicitudes HTTP " + r.code());
    }
}
