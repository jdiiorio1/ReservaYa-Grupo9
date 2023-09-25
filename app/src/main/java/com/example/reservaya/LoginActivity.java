package com.example.reservaya;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    private Button inicio;
    private Button registro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inicio = findViewById(R.id.bt_inicio);
        registro = findViewById(R.id.bt_registro);

        inicio.setOnClickListener(inicioListener);
        registro.setOnClickListener(registroListener);


    }

    public View.OnClickListener inicioListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intentHome = new Intent(LoginActivity.this, Home.class);
            startActivity(intentHome);
            LoginActivity.this.finish();
        }
    };

    public View.OnClickListener registroListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intentRegistrar = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intentRegistrar);
            LoginActivity.this.finish();
        }
    };

}