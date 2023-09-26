package com.example.reservaya;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private Button inicio;
    private Button registro;
    private EditText correo, pass;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inicio = findViewById(R.id.bt_inicio);
        registro = findViewById(R.id.bt_registro);
        correo= findViewById(R.id.et_mail);
        pass= findViewById(R.id.et_password);

        inicio.setOnClickListener(inicioListener);
        registro.setOnClickListener(registroListener);

    }

    public View.OnClickListener inicioListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (correo.getText().toString().isEmpty() || pass.getText().toString().isEmpty()){
                Toast.makeText(getApplicationContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LoginActivity.this, "Sesión iniciada", Toast.LENGTH_SHORT).show();
                Intent intentPropietario = new Intent(LoginActivity.this, PropietarioActivity.class);
                startActivity(intentPropietario);
                LoginActivity.this.finish();
            }
        }
    };

    public void consultarUsuario (View view) {
        requestQueue = Volley.newRequestQueue(this);
        String URL = "http://192.168.1.43/backend/consultarUsuario.php?correo_electronico="+correo.getText().toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("correo_electronico") && response.has("contraseña")) {
                                String correo = response.getString("correo_electronico");
                                String password = response.getString("contrasena");
                                if (password.equals(pass.getText().toString())) {
                                    Toast.makeText(LoginActivity.this, "Sesión iniciada", Toast.LENGTH_SHORT).show();
                                    Intent intentHome = new Intent(LoginActivity.this, Home.class);
                                    startActivity(intentHome);
                                    LoginActivity.this.finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "El usuario ingresado no existe", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "Error al consultar el usuario", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }


    public View.OnClickListener registroListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intentRegistrar = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intentRegistrar);
            LoginActivity.this.finish();
        }
    };

}