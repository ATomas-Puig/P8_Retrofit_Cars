package com.example.p8retrofitcars;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.p8retrofitcars.databinding.FragmentSupercarsBinding;
import com.example.p8retrofitcars.databinding.ViewholderContenidoBinding;

import java.util.List;

public class SupercarsFragment extends Fragment {
    private FragmentSupercarsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return (binding = FragmentSupercarsBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupercarsViewModel supercarsViewModel = new ViewModelProvider(this).get(SupercarsViewModel.class);

        // Aquí estabas observando 2 veces el "respuestaMutableLiveData": una para "loggear" el resultado y otra para establecer la lista en el adaptador.
        // Solo se debe observar una vez.

        // lo del "forEach" es porque se introdujo en la versión 8 de Java, pero en el proyecto está configurada la 7.
        // Lo he cambiado abajo por un "for" normal (aunque realmente no hace falta, ya que solo era para "loggear" el resultado)

//        supercarsViewModel.respuestaMutableLiveData.observe(getViewLifecycleOwner(), new Observer<Supercars.Respuesta>() {
//            //Gerard, aquí me obliga a poner esta anotación para que respuesta.results.forEach funcione y no sé por qué
//            @RequiresApi(api = Build.VERSION_CODES.N)
//            @Override
//            public void onChanged(Supercars.Respuesta respuesta) {
//                respuesta.results.forEach(coche -> Log.e("ABCD", coche.cocheFields.marca + ", " + coche.cocheFields.modelo + ", " + coche.cocheFields.imagen));
//            }
//        });

        ContenidosAdapter contenidosAdapter = new ContenidosAdapter();
        binding.recyclerviewContenidos.setAdapter(contenidosAdapter);


        // se debe llamar al método "buscar()" para que haga la petición a la API
        supercarsViewModel.buscar();

        // Cuidado porque tenías "SupercarsViewModel" con la "S" en mayúscula, con lo cual estabas accediendo a la clase, y no al objeto (por eso te obligaba a hacerlo "static")
        // Hay que poner la "s" minúscula para acceder al objeto "superCarsViewModel"
        supercarsViewModel.respuestaMutableLiveData.observe(getViewLifecycleOwner(), new Observer<Supercars.Respuesta>() {
            @Override
            public void onChanged(Supercars.Respuesta respuesta) {
                //Gerard, aquí me dice que respuesta.results espera List<CocheFields> pero recibe List<Coche> y no sé si Supercars.java está bien enfocado

                // Realmente la lista debe ser List<Coche> y luego cada objeto coche tiene un campo "name" y otro campo "fields"
                contenidosAdapter.establecerListaContenido(respuesta.documents);

                for(Supercars.Coche coche: respuesta.documents){
                    Log.e("ABCD", coche.fields.marca.stringValue + ", " + coche.fields.modelo.stringValue + ", " + coche.fields.imagen.stringValue);
                }
            }
        });
    }

    static class ContenidoViewHolder extends RecyclerView.ViewHolder {
        ViewholderContenidoBinding binding;

        public ContenidoViewHolder(@NonNull ViewholderContenidoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    class ContenidosAdapter extends RecyclerView.Adapter<ContenidoViewHolder>{
        // cambiado el array a List<Supercars.Coche>
        List<Supercars.Coche> coches;

        @NonNull
        @Override
        public ContenidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ContenidoViewHolder(ViewholderContenidoBinding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ContenidoViewHolder holder, int position) {
            Supercars.Coche coche = coches.get(position);

            holder.binding.marca.setText(coche.fields.marca.stringValue);
            holder.binding.modelo.setText(coche.fields.modelo.stringValue);
            Glide.with(requireActivity()).load(coche.fields.imagen.stringValue).into(holder.binding.imagen);
        }

        @Override
        public int getItemCount() {
            return coches == null ? 0 : coches.size();
        }

        void establecerListaContenido(List<Supercars.Coche> coches){
            this.coches = coches;
            notifyDataSetChanged();
        }
    }
}