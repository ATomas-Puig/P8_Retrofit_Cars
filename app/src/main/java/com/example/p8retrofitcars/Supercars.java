package com.example.p8retrofitcars;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class Supercars {

    class Respuesta {
        List<Coche> results;
    }

    class Coche {
        CocheFields cocheFields;
    }

    class CocheFields {
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

    public interface Api {
        @GET("/Supercars")
        Call<Respuesta> buscar();
    }
}
