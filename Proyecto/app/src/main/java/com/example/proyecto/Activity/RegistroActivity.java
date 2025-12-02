package com.example.proyecto.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyecto.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity {

    EditText txtEmail, txtPass, txtNombre, txtFecha, txtTelefono;
    Button btnRegistrar;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);


        txtEmail = findViewById(R.id.email);
        txtPass = findViewById(R.id.password);
        txtNombre = findViewById(R.id.nombre);
        txtFecha = findViewById(R.id.FechaN);
        txtTelefono = findViewById(R.id.telefono);
        btnRegistrar = findViewById(R.id.crear_cuenta);

        requestQueue = Volley.newRequestQueue(this);

        btnRegistrar.setOnClickListener(v -> registrarConVolley());
    }

    private void registrarConVolley() {

        String nombre = txtNombre.getText().toString().trim();
        String correo = txtEmail.getText().toString().trim();
        String contraseña = txtPass.getText().toString().trim();
        String telefono = txtTelefono.getText().toString().trim();
        String fecha = txtFecha.getText().toString().trim();

        if (nombre.isEmpty() || correo.isEmpty() || contraseña.isEmpty() ||
                telefono.isEmpty() || fecha.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://10.0.2.2:8080/AplicacionesMoviles/usuarios/";

        Map<String, String> params = new HashMap<>();
        params.put("nombre", nombre);
        params.put("correo", correo);
        params.put("contraseña", contraseña);
        params.put("telefono", telefono);
        params.put("fechanacimiento", fecha);

        JSONObject jsonObject = new JSONObject(params);

        Log.d("VOLLEY_REGISTRO", "Enviando JSON: " + jsonObject.toString());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("VOLLEY_REGISTRO", "Respuesta: " + response.toString());

                        try {
                            boolean success = response.getBoolean("success");
                            String mensaje = response.getString("mensaje");

                            if (success) {
                                Toast.makeText(RegistroActivity.this, mensaje, Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            } else {
                                Toast.makeText(RegistroActivity.this, "Error: " + mensaje, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RegistroActivity.this, "Error al procesar respuesta", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY_REGISTRO", "Error: " + error.toString());

                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            try {
                                String errorBody = new String(error.networkResponse.data, "utf-8");
                                Log.e("VOLLEY_REGISTRO", "Error body: " + errorBody);

                                JSONObject errorJson = new JSONObject(errorBody);
                                String errorMsg = errorJson.getString("mensaje");
                                Toast.makeText(RegistroActivity.this, "Error: " + errorMsg, Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(RegistroActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(RegistroActivity.this, "Error de conexión: " + error.getMessage(), Toast.LENGTH_LONG).show();
                        }
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
}