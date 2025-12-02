package com.example.proyecto.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.content.SharedPreferences;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyecto.Fragment.FragmentActivity;
import com.example.proyecto.R;

public class MainActivity extends AppCompatActivity {

    private Button accederButton;
    private TextView crearCuentaText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            Intent intent = new Intent(MainActivity.this, FragmentActivity.class);
            startActivity(intent);
            finish();
        } else {

            setContentView(R.layout.activity_main);

            accederButton = findViewById(R.id.acceder);
            crearCuentaText = findViewById(R.id.crear_cuenta);

            accederButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            crearCuentaText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
}
