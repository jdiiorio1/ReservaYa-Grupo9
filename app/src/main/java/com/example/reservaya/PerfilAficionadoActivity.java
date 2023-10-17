package com.example.reservaya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PerfilAficionadoActivity extends AppCompatActivity {
    private Button volver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_aficionado);

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
}