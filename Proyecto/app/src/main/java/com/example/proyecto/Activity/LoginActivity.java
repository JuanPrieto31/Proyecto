package com.example.proyecto.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyecto.Fragment.FragmentActivity;
import com.example.proyecto.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText txtEmail, txtPass;
    Button btnLogin, btnIrRegistro;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtEmail = findViewById(R.id.email);
        txtPass = findViewById(R.id.password);
        btnLogin = findViewById(R.id.acceder);
        btnIrRegistro = findViewById(R.id.crear_cuenta);

        requestQueue = Volley.newRequestQueue(this);

        btnLogin.setOnClickListener(v -> loginConVolley());
        btnIrRegistro.setOnClickListener(v -> startActivity(new Intent(this, RegistroActivity.class)));
    }

    private void loginConVolley() {
        String correo = txtEmail.getText().toString().trim();
        String contraseña = txtPass.getText().toString().trim();

        if (correo.isEmpty() || contraseña.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://10.0.2.2:8080/AplicacionesMoviles/usuarios/login";

        Map<String, String> params = new HashMap<>();
        params.put("correo", correo);
        params.put("contraseña", contraseña);

        JSONObject jsonObject = new JSONObject(params);

        Log.d("VOLLEY_LOGIN", "Enviando JSON: " + jsonObject.toString());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("VOLLEY_LOGIN", "Respuesta: " + response.toString());

                        try {
                            boolean success = response.getBoolean("success");
                            String mensaje = response.getString("mensaje");

                            if (success) {
                                Toast.makeText(LoginActivity.this, "¡Bienvenido!", Toast.LENGTH_SHORT).show();
                                SharedPreferences sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("correo", correo);
                                editor.apply();
                                try {
                                    Intent intent = new Intent(LoginActivity.this, FragmentActivity.class);
                                    startActivity(intent);
                                    finish();
                                } catch (Exception e) {
                                    Log.e("VOLLEY_LOGIN", "Error al abrir MainActivity: " + e.getMessage());
                                    Toast.makeText(LoginActivity.this, "Error: MainActivity no encontrada", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, mensaje, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Error al procesar respuesta", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY_LOGIN", "Error: " + error.toString());

                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            try {
                                String errorBody = new String(error.networkResponse.data, "utf-8");
                                Log.e("VOLLEY_LOGIN", "Error body: " + errorBody);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        Toast.makeText(LoginActivity.this, "Error de conexión: " + error.getMessage(), Toast.LENGTH_LONG).show();
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