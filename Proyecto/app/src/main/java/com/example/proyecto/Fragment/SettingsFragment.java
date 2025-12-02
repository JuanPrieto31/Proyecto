package com.example.proyecto.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyecto.Activity.LoginActivity;
import com.example.proyecto.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SettingsFragment extends Fragment {

    private TextView nombreTextView, correoTextView, fechaNacimientoTextView;
    private Button cerrarSesionButton;
    ImageView imageView;
    private RequestQueue requestQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        cerrarSesionButton = view.findViewById(R.id.btncerrar);
        nombreTextView = view.findViewById(R.id.nombrec);
        correoTextView = view.findViewById(R.id.correoc);
        fechaNacimientoTextView = view.findViewById(R.id.fechanacimientoc);
        imageView = view.findViewById(R.id.imageview);

        requestQueue = Volley.newRequestQueue(requireActivity());
        obtenerDatosUsuario();
        cerrarSesionButton.setOnClickListener(v -> cerrarSesion());

        Picasso.get()
                .load("https://cdn.pixabay.com/photo/2023/02/18/11/00/icon-7797704_1280.png")
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(imageView);

        return view;
    }

    private void obtenerDatosUsuario() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String correo = sharedPreferences.getString("correo", null);

        if (correo == null || correo.isEmpty()) {
            Toast.makeText(getActivity(), "No hay sesión activa", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://10.0.2.2:8080/AplicacionesMoviles/usuarios/perfil?correo=" + correo;

        Log.d("SETTINGS_FRAGMENT", "Obteniendo datos para: " + correo);
        Log.d("SETTINGS_FRAGMENT", "URL: " + url);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("SETTINGS_FRAGMENT", "Respuesta GET: " + response.toString());

                        try {
                            boolean success = response.getBoolean("success");

                            if (success) {
                                JSONObject usuario = response.getJSONObject("usuario");

                                String nombre = usuario.getString("nombre");
                                String email = usuario.getString("correo");
                                String fechaNacimiento = usuario.getString("fechanacimiento");

                                nombreTextView.setText("Nombre: " + nombre);
                                correoTextView.setText("Correo: " + email);
                                fechaNacimientoTextView.setText("Fecha Nacimiento: " + fechaNacimiento);

                                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("nombre", nombre);
                                editor.putString("email", email);
                                editor.putString("fechaNacimiento", fechaNacimiento);
                                editor.apply();

                            } else {
                                String mensaje = response.getString("mensaje");
                                Toast.makeText(getActivity(), "Error: " + mensaje, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Error al procesar datos", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("SETTINGS_FRAGMENT", "Error en GET: " + error.toString());

                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            try {
                                String errorBody = new String(error.networkResponse.data, "utf-8");
                                Log.e("SETTINGS_FRAGMENT", "Error body: " + errorBody);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        Toast.makeText(getActivity(), "Error al cargar datos", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                10000,
                com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(request);
    }
    private void cerrarSesion() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }
}