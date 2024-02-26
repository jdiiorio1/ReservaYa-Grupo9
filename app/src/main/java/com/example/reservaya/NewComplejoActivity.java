package com.example.reservaya;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;


public class NewComplejoActivity extends AppCompatActivity {

    EditText nombreComplejo, hora_ini, hora_fin, calle, numero, localidad, codigoPostal, provincia;

    Button guardar, ubicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_complejo);

        nombreComplejo= findViewById(R.id.cj_name);
        hora_ini=findViewById(R.id.hora_ini);
        hora_fin=findViewById(R.id.hora_fin);
        calle=findViewById(R.id.calle);
        numero=findViewById(R.id.numero);
        localidad=findViewById(R.id.localidad);
        codigoPostal=findViewById(R.id.codigoPostal);
        provincia=findViewById(R.id.provincia);
        guardar=findViewById(R.id.btn_guardar);
        ubicacion=findViewById(R.id.btn_gps);


        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        setTitle("Nuevo Complejo");
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
                .setTitle("Salir")
                .setMessage("¿Estás seguro de que quieres cancelar la creacion de un nuevo complejo?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = getIntent();
                        int idUsuario = intent.getIntExtra("id_Usuario", 0);
                        Intent intentHome = new Intent(NewComplejoActivity.this, PropietarioActivity.class);
                        intentHome.putExtra("id", idUsuario);
                        startActivity(intentHome);
                        finish();

                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}