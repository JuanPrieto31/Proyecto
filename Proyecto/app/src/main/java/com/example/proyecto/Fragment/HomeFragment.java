package com.example.proyecto.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyecto.R;
import com.example.proyecto.adaptadores.HeroeAdaptador;
import com.example.proyecto.clases.Heroe;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    List<Heroe> listaHeroes;

    private final String BASE_URL = "https://superheroapi.com/api/f6bf37d1dee3137a26c33d01467b09f4/";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        listaHeroes = new ArrayList<>();

        cargarHeroes();

        return view;
    }

    private void cargarHeroes() {

        for (int id = 1; id <= 30; id++) {

            StringRequest request = new StringRequest(Request.Method.GET, BASE_URL + id,
                    response -> {
                        try {
                            JSONObject obj = new JSONObject(response);

                            String nombre = obj.getString("name");
                            String imagen = obj.getJSONObject("image").getString("url");
                            String publicador = obj.getJSONObject("biography").getString("publisher");
                            String origen = obj.getJSONObject("biography").getString("place-of-birth");
                            String poder = obj.getJSONObject("powerstats").getString("power");
                            String comics = obj.getJSONObject("work").getString("occupation");

                            Heroe h = new Heroe(
                                    nombre,
                                    poder,
                                    publicador,
                                    imagen,
                                    "Información del héroe proporcionada por SuperHeroAPI.",
                                    comics,
                                    origen
                            );

                            listaHeroes.add(h);

                            recyclerView.setAdapter(new HeroeAdaptador(requireContext(), listaHeroes));

                        } catch (JSONException e) {
                            Toast.makeText(requireContext(), "Error procesando héroe", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> Toast.makeText(requireContext(), "Error con la API", Toast.LENGTH_SHORT).show()
            );

            Volley.newRequestQueue(requireContext()).add(request);
        }
    }
}
