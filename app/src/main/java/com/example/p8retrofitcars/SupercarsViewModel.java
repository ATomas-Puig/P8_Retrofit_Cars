package com.example.p8retrofitcars;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupercarsViewModel extends AndroidViewModel {

    // aquí el "static" ya no es necesario, porque en el fragment ahora se accede a través del objeto, y no de la clase

    MutableLiveData<Supercars.Respuesta> respuestaMutableLiveData = new MutableLiveData<>();

    public SupercarsViewModel(@NonNull Application application) {
        super(application);
    }


    // El parámetro "texto" no es necesario, ya que no se van a buscar "coches"
    public void buscar(){
        Supercars.api.buscar().enqueue(new Callback<Supercars.Respuesta>() {
            @Override
            public void onResponse(@NonNull Call<Supercars.Respuesta> call, @NonNull Response<Supercars.Respuesta> response) {
                respuestaMutableLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Supercars.Respuesta> call, @NonNull Throwable t) {}
        });
    }
}