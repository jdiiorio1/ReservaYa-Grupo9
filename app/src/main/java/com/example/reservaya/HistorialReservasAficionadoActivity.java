package com.example.reservaya;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HistorialReservasAficionadoActivity extends AppCompatActivity {

    /*
Declarar instancias globales
*/
    private RequestQueue requestQueue;
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;

    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_reservas_aficionado);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            idUsuario = extras.getInt("idUsuario");
            Log.i("logReserva", "el usuario para visualizar el historial es " +  idUsuario);
        }

        // Obtener las reservas realizadas por el usuario desde la base de datos
        requestQueue = Volley.newRequestQueue(this);

        String URL = "http://192.168.1.35/backend/cargarHistorialAficionado.php?id_usuario="+ idUsuario;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Log.i("logReserva", "entro al for");
                            JSONArray jsonArray = response;
                            // Inicializar complejos
                            List items = new ArrayList();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String nombreComplejo = object.getString("nombre");
                                String capacidad = object.getString("capacidad");
                                String techada = object.getString("techado");
                                String fecha = object.getString("fecha");
                                String hora = object.getString("hora");
                                String costo = object.getString("costo");

                                //Toast.makeText(Home.this, "complejo:" + nombre, Toast.LENGTH_SHORT).show();
                                items.add(new HistorialReserva(nombreComplejo, capacidad, techada, fecha, hora, costo));

                                Log.i("logReserva", "item: " + nombreComplejo + " cancha de " + capacidad + " " + costo);

                            }
                            // Obtener el Recycler
                            recycler = (RecyclerView) findViewById(R.id.rv_historial_aficionado);
                            recycler.setHasFixedSize(true);

                            // Usar un administrador para LinearLayout
                            lManager = new LinearLayoutManager(getApplicationContext());
                            recycler.setLayoutManager(lManager);

                            // Crear un nuevo adaptador
                            final HistorialReservaAdapter adapter = new HistorialReservaAdapter(items);
                            recycler.setAdapter(adapter);

                            int resId = R.anim.layout_animation_rotate_in;
                            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(HistorialReservasAficionadoActivity.this, resId);
                            recycler.setLayoutAnimation(animation);
                            adapter.notifyDataSetChanged();


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HistorialReservasAficionadoActivity.this, "Error al obtener las reservas", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);

    }
}