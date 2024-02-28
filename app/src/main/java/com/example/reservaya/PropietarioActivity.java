package com.example.reservaya;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.reservaya.LoginActivity;
import com.example.reservaya.NewComplejoActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

public class PropietarioActivity extends AppCompatActivity {

    private int propietarioId;
    private int usuarioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propietario);

        // Obtener el idUsuario del Intent
        Intent intent = getIntent();
        usuarioId = intent.getIntExtra("id_usuario", 0);

        // Inicializar Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        setTitle("Tus Propiedades");

        // Configurar FloatingActionButton
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PropietarioActivity.this, NewComplejoActivity.class);
                intent.putExtra("id_usuario", usuarioId);
                intent.putExtra("id_propietario", propietarioId);
                startActivity(intent);
            }
        });

        // Consultar datos del propietario
        consultarDatosPropietario(usuarioId);
    }

    // Método para realizar la consulta de datos del propietario
    private void consultarDatosPropietario(int usuarioId) {
        String URL = "http://192.168.1.53/backend/consultarPropietario.php?usuario_id=" + usuarioId;

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Procesar la respuesta del servidor y obtener los datos del propietario
                            propietarioId = response.getInt("id");
                            // No necesitas usuarioId ya que ya lo tienes
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        showLogoutConfirmationDialog();
    }

    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Cerrar sesión")
                .setMessage("¿Estás seguro de que quieres cerrar sesión?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        startActivity(new Intent(PropietarioActivity.this, LoginActivity.class));
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
