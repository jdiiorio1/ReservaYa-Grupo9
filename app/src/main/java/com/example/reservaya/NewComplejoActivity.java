package com.example.reservaya;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class NewComplejoActivity extends AppCompatActivity {

    EditText nombreComplejo, hora_ini, hora_fin, calle, numero, localidad, codigoPostal, provincia;

    Button guardar, ubicacion;

    Integer propietario_id, usuario_id;

    Double latitud, longitud;

    private static final String urlInsertarComplejo="http://192.168.1.53/backend/insertarComplejo.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_complejo);

        nombreComplejo = findViewById(R.id.cj_name);
        hora_ini = findViewById(R.id.hora_ini);
        hora_fin = findViewById(R.id.hora_fin);
        calle = findViewById(R.id.calle);
        numero = findViewById(R.id.numero);
        localidad = findViewById(R.id.localidad);
        codigoPostal = findViewById(R.id.codigoPostal);
        provincia = findViewById(R.id.provincia);
        guardar = findViewById(R.id.btn_guardar);
        ubicacion = findViewById(R.id.btn_gps);
        Log.i("Hora", hora_ini.getText().toString());

        // Obtener el ID del propietario pasado desde la pantalla anterior
        Intent intent = getIntent();
        propietario_id = intent.getIntExtra("id_propietario", 0); // 0 es el valor predeterminado si no se encuentra el extra
        usuario_id= intent.getIntExtra("id_usuario", 0);

        guardar.setOnClickListener(guardarListener);
        ubicacion.setOnClickListener(ubicacionListener);

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
                .setMessage("¿Estás seguro de que quieres cancelar la creación de un nuevo complejo?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intentHome = new Intent(NewComplejoActivity.this, PropietarioActivity.class);
                        intentHome.putExtra("id_usuario", usuario_id);
                        intentHome.putExtra("id_propietario", propietario_id);
                        startActivity(intentHome);
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private class GetLocationTask extends AsyncTask<String, Void, LatLng> {
        @Override
        protected LatLng doInBackground(String... addresses) {
            String address = addresses[0];
            try {
                address = address.replaceAll(" ", "+");
                URL url = new URL("https://nominatim.openstreetmap.org/search?q=" + address + "&format=json");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Obtener la respuesta del servidor
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }
                bufferedReader.close();

                // Parsear la respuesta JSON
                JSONArray jsonArray = new JSONArray(response.toString());
                if (jsonArray.length() > 0) {
                    JSONObject location = jsonArray.getJSONObject(0);

                    latitud = location.getDouble("lat");
                    longitud = location.getDouble("lon");
                    Log.i("ubicacion", latitud.toString());
                    Log.i("ubicacion", longitud.toString());
                    return new LatLng(latitud, longitud);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(LatLng result) {
            // Aquí puedes manejar el resultado, por ejemplo, actualizar la interfaz de usuario
            if (result != null) {
                // Haz algo con la ubicación
                guardarComplejoDespuesDeUbicacion();
            } else {
                // Maneja el caso de que no se pueda obtener la ubicación
                Toast.makeText(NewComplejoActivity.this, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public View.OnClickListener ubicacionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i("id propietario",propietario_id.toString());
        }
    };

    public View.OnClickListener guardarListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            guardarComplejo();
            loginActivity(v);
        }
    };

    public void loginActivity(View view) {
        // Crear una intención para ir a OtraActivity
        Intent intent = new Intent(this, PropietarioActivity.class);
        intent.putExtra("id_propietario", propietario_id);
        intent.putExtra("id_usuario", usuario_id);
        startActivity(intent);
    }

    private void guardarComplejo() {
        // Obtener la dirección completa
        String direccionCompleta = calle.getText().toString() + " " + numero.getText().toString() + ", " + localidad.getText().toString() + ", " + provincia.getText().toString() + " " + codigoPostal.getText().toString();

        // Obtener la ubicación a partir de la dirección
        new GetLocationTask().execute(direccionCompleta);
    }

    private void guardarComplejoDespuesDeUbicacion() {
        RequestQueue requestQueue = Volley.newRequestQueue(NewComplejoActivity.this);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                urlInsertarComplejo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "El complejo fue creado con éxito", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error, el complejo no fue creado.", Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nombre", nombreComplejo.getText().toString());
                params.put("hora_inicio", hora_ini.getText().toString());
                params.put("hora_fin", hora_fin.getText().toString());
                params.put("calle", calle.getText().toString());
                params.put("numero", numero.getText().toString());
                params.put("nombreLocalidad", localidad.getText().toString());
                params.put("codigoPostal", codigoPostal.getText().toString());
                params.put("provincia", provincia.getText().toString());
                params.put("propietario_id", propietario_id.toString());
                params.put("latitud", String.valueOf(latitud)); // Convertir a String
                params.put("longitud", String.valueOf(longitud)); // Convertir a String
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
