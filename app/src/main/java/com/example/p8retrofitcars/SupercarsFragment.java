package com.example.p8retrofitcars;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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

        supercarsViewModel.respuestaMutableLiveData.observe(getViewLifecycleOwner(), new Observer<Supercars.Respuesta>() {
            //Gerard, aquí me obliga a poner esta anotación para que respuesta.results.forEach funcione y no sé por qué
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(Supercars.Respuesta respuesta) {
                respuesta.results.forEach(coche -> Log.e("ABCD", coche.cocheFields.marca + ", " + coche.cocheFields.modelo + ", " + coche.cocheFields.imagen));
            }
        });

        ContenidosAdapter contenidosAdapter = new ContenidosAdapter();
        binding.recyclerviewContenidos.setAdapter(contenidosAdapter);

        SupercarsViewModel.respuestaMutableLiveData.observe(getViewLifecycleOwner(), new Observer<Supercars.Respuesta>() {
            @Override
            public void onChanged(Supercars.Respuesta respuesta) {
                //Gerard, aquí me dice que respuesta.results espera List<CocheFields> pero recibe List<Coche> y no sé si Supercars.java está bien enfocado
                contenidosAdapter.establecerListaContenido(respuesta.results);
                // respuesta.results.forEach(r -> Log.e("ABCD", r.artistName + ", " + r.trackName + ", " + r.artworkUrl100));
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
        List<Supercars.CocheFields> cocheFieldsList;

        @NonNull
        @Override
        public ContenidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ContenidoViewHolder(ViewholderContenidoBinding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ContenidoViewHolder holder, int position) {
            Supercars.CocheFields cocheFields = cocheFieldsList.get(position);

            holder.binding.marca.setText(cocheFields.marca.stringValue);
            holder.binding.modelo.setText(cocheFields.modelo.stringValue);
            Glide.with(requireActivity()).load(cocheFields.imagen).into(holder.binding.imagen);
        }

        @Override
        public int getItemCount() {
            return cocheFieldsList == null ? 0 : cocheFieldsList.size();
        }

        void establecerListaContenido(List<Supercars.CocheFields> cocheFieldsList){
            this.cocheFieldsList = cocheFieldsList;
            notifyDataSetChanged();
        }
    }
}