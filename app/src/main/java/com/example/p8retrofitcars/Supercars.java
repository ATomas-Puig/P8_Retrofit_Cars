package com.example.p8retrofitcars;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class Supercars {

    // las clases para modelar el JSON
    class Respuesta {
        List<Coche> documents;
    }

    class Coche {
        String name;
        CocheFields fields;
    }

    class CocheFields{
        StringValue marca;
        StringValue modelo;
        StringValue imagen;
    }

    class StringValue{
        String stringValue;
    }


    public static Api api = new Retrofit.Builder()
            .baseUrl("https://firestore.googleapis.com/v1/projects/p8retrofit/databases/(default)/documents/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api.class);


    // aquí no hay que poner la barra, para que así la ruta sea "relativa" a la baseUrl
    public interface Api {
        @GET("Supercars")
        Call<Respuesta> buscar();
    }
}
