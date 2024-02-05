package com.example.reservaya;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservaCanchas extends AppCompatActivity {

    /*
    Declarar instancias globales
    */
    private RequestQueue requestQueue;
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private String complejoId;
    private String fecha;
    private String hora;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva_canchas);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
             complejoId = extras.getString("complejoId");
             fecha = extras.getString("fecha");
             hora = extras.getString("hora");
        }

        Toast.makeText(ReservaCanchas.this, "complejo id:" + complejoId + " Fecha" + fecha + " Hora:" + hora, Toast.LENGTH_SHORT).show();

        // Obtener las canchas disponibles desde la bsae de datos
        requestQueue = Volley.newRequestQueue(this);
        String URL = "http://192.168.1.35/backend/cargarCanchasFiltradas.php?id_complejo="+ complejoId +"&fecha=" + fecha + "&hora=" + hora;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONArray jsonArray = response;
                            // Inicializar complejos
                            List items = new ArrayList();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String idCancha = object.getString("id");
                                String capacidad = object.getString("capacidad");
                                String techada = object.getString("techado");

                                //Toast.makeText(Home.this, "complejo:" + nombre, Toast.LENGTH_SHORT).show();
                                items.add(new Cancha(idCancha, capacidad, techada));


                            }
                            // Obtener el Recycler
                            recycler = (RecyclerView) findViewById(R.id.reciclador);
                            recycler.setHasFixedSize(true);

                            // Usar un administrador para LinearLayout
                            lManager = new LinearLayoutManager(getApplicationContext());
                            recycler.setLayoutManager(lManager);

                            // Crear un nuevo adaptador
                            final CanchasAdapter adapter = new CanchasAdapter(items);
                            recycler.setAdapter(adapter);

                            int resId = R.anim.layout_animation_rotate_in;
                            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(ReservaCanchas.this, resId);
                            recycler.setLayoutAnimation(animation);
                            adapter.notifyDataSetChanged();

                            adapter.setOnClickListener(new CanchasAdapter.OnClickListener() {
                                @Override
                                public void onClick(int position, Cancha model) {

                                    AlertDialog.Builder dialogoConfirmacion = new AlertDialog.Builder(ReservaCanchas.this);
                                    dialogoConfirmacion.setTitle("Confirmar reserva");

                                    dialogoConfirmacion.setMessage("Esta por reservar esta cancha de " +model.getCapacidad()+ " para el dia " + fecha + " a las " + hora + " horas");
                                    dialogoConfirmacion.setCancelable(false);
                                    dialogoConfirmacion.setPositiveButton("Reservar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            realizarReserva(model.getId(), complejoId, fecha, hora);
                                            //Toast.makeText(ReservaCanchas.this, "Reservada", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    dialogoConfirmacion.setNegativeButton("no", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    AlertDialog alertDialog = dialogoConfirmacion.create();
                                    alertDialog.show();
                                }

                            });



                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ReservaCanchas.this, "Error al obtener las canchas", Toast.LENGTH_SHORT).show();
                        }
                    });

        requestQueue.add(jsonArrayRequest);

/*
        // Inicializar canchas
        List items = new ArrayList();

        items.add(new Cancha("5", "Si"));
        items.add(new Cancha("7", "No" ));
        items.add(new Cancha("5", "No"));
        items.add(new Cancha("5", "Si"));
        items.add(new Cancha("7", "Si"));
        items.add(new Cancha("5", "Si"));



// Obtener el Recycler
        recycler = (RecyclerView) findViewById(R.id.reciclador);
        recycler.setHasFixedSize(true);

// Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);

// Crear un nuevo adaptador
        adapter = new CanchasAdapter(items);
        recycler.setAdapter(adapter); */
    }


    public void realizarReserva(String idCancha, String idComplejo, String fecha, String hora) {

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                "http://192.168.1.35/backend/insertarReservaEnAgenda.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(ReservaCanchas.this,"Se realizó su reserva", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ReservaCanchas.this,"Por favor intente mas tarde", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() {
                Map <String, String> params = new HashMap<>();
                params.put("id_cancha", idCancha);
                params.put("id_complejo", idComplejo);
                params.put("fecha", fecha);
                params.put("hora", hora);

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}