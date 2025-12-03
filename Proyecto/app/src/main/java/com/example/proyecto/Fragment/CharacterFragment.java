package com.example.proyecto.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyecto.R;
import com.example.proyecto.adaptadores.HeroeAdaptador;
import com.example.proyecto.clases.Heroe;
import com.google.android.material.textfield.TextInputEditText;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class CharacterFragment extends Fragment {
    RecyclerView recyclerView;
    List<Heroe> listaHeroes;
    HeroeAdaptador adaptador;
    TextInputEditText searchBar;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

    private final String BASE_URL = "https://superheroapi.com/api/f6bf37d1dee3137a26c33d01467b09f4/";
    private final String SEARCH_URL = "https://superheroapi.com/api/f6bf37d1dee3137a26c33d01467b09f4/search/";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        searchBar = view.findViewById(R.id.searchBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        listaHeroes = new ArrayList<>();
        adaptador = new HeroeAdaptador(requireContext(), listaHeroes);
        recyclerView.setAdapter(adaptador);

        configurarBuscador();
        return view;
    }

    private void configurarBuscador() {
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String texto = s.toString().trim();


                if (searchRunnable != null) {
                    handler.removeCallbacks(searchRunnable);
                }

                searchRunnable = new Runnable() {
                    @Override
                    public void run() {
                        if (texto.length() > 0) {
                            buscarHeroe(texto);
                        } else if (texto.isEmpty()) {
                            listaHeroes.clear();
                            adaptador.notifyDataSetChanged();
                        }
                    }
                };
                handler.postDelayed(searchRunnable, 500);
            }
        });
    }

    private void buscarHeroe(String texto) {
        listaHeroes.clear();
        if (texto.matches("\\d+")) {
            buscarPorId(texto);
        } else {
            buscarPorNombre(texto);
        }
    }

    private void buscarPorId(String id) {
        StringRequest request = new StringRequest(Request.Method.GET, BASE_URL + id,
                response -> {
                    try {
                        procesarHeroe(response);
                    } catch (JSONException e) {
                        Toast.makeText(requireContext(), "Error procesando héroe", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(requireContext(), "Error con la API", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(requireContext()).add(request);
    }

    private void buscarPorNombre(String nombre) {

        String nombreBusqueda = nombre.replace(" ", "-");

        StringRequest request = new StringRequest(Request.Method.GET, SEARCH_URL + nombreBusqueda,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);

                        if (obj.has("error")) {
                            Toast.makeText(requireContext(), "Héroe no encontrado", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (obj.has("results")) {
                            JSONObject heroeData = obj.getJSONArray("results").getJSONObject(0);
                            procesarHeroe(heroeData.toString());
                        }
                    } catch (JSONException e) {
                        Toast.makeText(requireContext(), "Error procesando resultados", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(requireContext(), "Error con la API", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(requireContext()).add(request);
    }

    private void procesarHeroe(String response) throws JSONException {
        JSONObject obj = new JSONObject(response);

        String nombre = obj.getString("name");
        String imagen = obj.getJSONObject("image").getString("url");
        String publicador = obj.getJSONObject("biography").getString("publisher");
        String origen = obj.getJSONObject("biography").getString("place-of-birth");
        String poder = obj.getJSONObject("powerstats").getString("power");
        String ocupacion = obj.getJSONObject("work").getString("occupation");

        Heroe h = new Heroe(
                nombre,
                poder,
                publicador,
                imagen,
                "Información del héroe proporcionada por SuperHeroAPI.",
                ocupacion,
                origen
        );

        listaHeroes.clear();
        listaHeroes.add(h);
        adaptador.notifyDataSetChanged();
    }
}