package com.example.reservaya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PerfilAficionadoActivity extends AppCompatActivity {
    private Button volver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_aficionado);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        setTitle("Editá tu perfil");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottonNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bt_historial_aficionado);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.bt_historial_aficionado) {
                return true;
            } else if (id == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), Home.class));
                overridePendingTransition(R.anim.slide_out_izq, R.anim.slide_in_der);
                finish();
                return true;
            } else {
                return false;
            }
        });

        /*
         * Asocio las instancias a la interfaz
         * */
        volver = findViewById(R.id.bt_volver_home);

        /*
         * Creo los listener de los elementos clickeables
         * */
        volver.setOnClickListener(volverHomeListener);
    }

    public View.OnClickListener volverHomeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intentHome = new Intent(PerfilAficionadoActivity.this, Home.class);
            startActivity(intentHome);
            PerfilAficionadoActivity.this.finish();
        }
    };

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
}