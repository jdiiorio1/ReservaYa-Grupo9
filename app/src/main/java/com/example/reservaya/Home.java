package com.example.reservaya;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;
import org.osmdroid.views.overlay.mylocation.DirectedLocationOverlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import android.preference.PreferenceManager;
import android.service.autofill.OnClickAction;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class Home extends AppCompatActivity {

    private int idUsuario;

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private TextView tvCalendario;
    private TextView tvHorario;
    private MapView         mMapView;
    private MapController   mMapController;

    private LocationManager locationManager;

    private RequestQueue requestQueue;
    private LinearLayout parentLayout;

    // Default map zoom level:
    private int MAP_DEFAULT_ZOOM = 14;

    // Default map Latitude:
    private double MAP_DEFAULT_LATITUDE = -34.90445;

    // Default map Longitude:
    private double MAP_DEFAULT_LONGITUDE = -57.92529;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idUsuario = extras.getInt("id_usuario");
            Log.i("extra", "se llego al home con un extra " +  idUsuario);

        }
        Log.i("extra", "el usuario es " +  idUsuario);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottonNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            int id = item.getItemId();
            if (id == R.id.bottom_home) {
                return true;
            } else if (id == R.id.bottom_edit) {
                startActivity(new Intent(getApplicationContext(), PerfilAficionadoActivity.class));
                overridePendingTransition(R.anim.slide_out_izq, R.anim.slide_in_der);
                finish();
                return true;
            } else {
                return false;
            }
        });

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        setTitle("Elegí donde jugar");


        if (Build.VERSION.SDK_INT >= 21) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                String[] permisos = {Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permisos, 1);
            }
        }

        // Obtener el Recycler
        recycler = (RecyclerView) findViewById(R.id.recicladorComplejos);
        recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(Home.this, LinearLayoutManager.HORIZONTAL, false);
        recycler.setLayoutManager(lManager);

        /*
         * Asocio las instancias a la interfaz
         * */
        tvCalendario = findViewById(R.id.tvfecha);
        tvHorario = findViewById(R.id.tvhora);

        /*
         * Creo los listener para la seleccion de fecha y horario
         * */
        tvCalendario.setOnClickListener(calendarioOnClickListener);
        tvHorario.setOnClickListener(horarioOnClickListener);


        //Donde muestra la imagen del mapa
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        mMapView = (MapView) findViewById(R.id.osmmap);
        mMapView.setTileSource(TileSourceFactory.MAPNIK);
        mMapView.setBuiltInZoomControls(true);
        mMapView.setMultiTouchControls(true);

        mMapController = (MapController)mMapView.getController();
        mMapController.setZoom(MAP_DEFAULT_ZOOM);

        GeoPoint center = new GeoPoint(MAP_DEFAULT_LATITUDE,MAP_DEFAULT_LONGITUDE);
        mMapController.animateTo(center);

        addMarker(center);

        //cargo la lista de complejos en el scroolview
      //  parentLayout = findViewById(R.id.contenedorListaComplejos);


        // consulto a la base de datos para obtener los complejos y agregar los marcadores en el mapa
        //cargarMapaConComplejos();



        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);



        mMapView.addMapListener(new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent event) {
                Log.i("Script", "onScroll()");
                return false;
            }

            @Override
            public boolean onZoom(ZoomEvent event) {
                Log.i("Script", "onZoom()");
                return false;
            }

        });


    }

    /*
    * Creo los selectores de fecha y horario
    *
     */

    public View.OnClickListener calendarioOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            int mAnio = c.get(Calendar.YEAR);
            int mMes = c.get(Calendar.MONTH);
            int mSemana = c.get(Calendar.WEEK_OF_YEAR);
            int mDia = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(Home.this, //R.style.Dialog_Theme_ReservaYa,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {


                            //tvCalendario.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            tvCalendario.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                        }
                    }, mAnio, mMes, mDia);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);


            datePickerDialog.show();

        }
    };

    public View.OnClickListener horarioOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            int mHora;
            if (tvHorario.getText().toString().equals("")) {
                mHora = c.get(Calendar.HOUR_OF_DAY);
            } else {
                mHora = Integer.parseInt(tvHorario.getText().toString());
            }

            int mMinuto = 0;

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(Home.this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            tvHorario.setText(String.valueOf(hourOfDay));
                            // consulto a la base de datos para obtener los complejos y agregar los marcadores en el mapa
                            mMapView.getOverlays().clear();
                            mMapView.clearFocus();

                            GeoPoint center = new GeoPoint(MAP_DEFAULT_LATITUDE,MAP_DEFAULT_LONGITUDE);
                            mMapController.animateTo(center);

                            addMarker(center);
                            cargarMapaConComplejos(tvCalendario.getText().toString(), tvHorario.getText().toString());

                        }
                    }, mHora, mMinuto, false);
            timePickerDialog.show();



        }
    };


    public void cargarMapaConComplejos (String fecha, String hora) {
        requestQueue = Volley.newRequestQueue(this);
        String URL = "http://192.168.1.35/backend/cargarComplejosFiltrados.php?fecha=" + fecha + "&hora=" + hora;
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
                                String idComplejo = object.getString("id");
                                String nombre = object.getString("nombre");
                                String latitud = object.getString("latitud");
                                String longitud = object.getString("longitud");
                                String cantCanchas = object.getString("cant_canchas");
                                //Toast.makeText(Home.this, "complejo:" + nombre, Toast.LENGTH_SHORT).show();
                                int imagenComplejo = getResources().getIdentifier("complejo_"+idComplejo, "drawable", getPackageName());
                                items.add(new Complejo(idComplejo, nombre, imagenComplejo, "Calle falsa 123", cantCanchas));


                                addMarker(new GeoPoint(Double.parseDouble(latitud), Double.parseDouble(longitud)), nombre, cantCanchas, idComplejo);

                                //cargo la lista de complejos en el scrollview
                          /*      LinearLayout linearLayout = new LinearLayout(Home.this);
                                linearLayout.setOrientation(LinearLayout.HORIZONTAL);


                                TextView tituloView = new TextView(Home.this);

                                tituloView.setBackground(ContextCompat.getDrawable(Home.this, R.drawable.input_box));
                                tituloView.setTextColor(ContextCompat.getColor(Home.this, R.color.white));
                                tituloView.setPadding(40,40,40,40);
                                tituloView.setId(Integer.parseInt(idComplejo));
                                int idTextview = tituloView.getId();
                                tituloView.setText(nombre + "\n" + "canchas disponibles: " + cantCanchas + "\n" + "Reserva ya");


                                tituloView.setOnClickListener(onComplejoClickListerner);
                                linearLayout.addView(tituloView);

                                parentLayout.addView(linearLayout);*/
                            }

                            // Crear un nuevo adaptador
                            final ComplejosAdapter adapter = new ComplejosAdapter(items);
                            recycler.setAdapter(adapter);

                            int resId = R.anim.layout_animation_rotate_in;
                            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(Home.this, resId);
                            recycler.setLayoutAnimation(animation);
                            adapter.notifyDataSetChanged();
                            adapter.setOnClickListener(new ComplejosAdapter.OnClickListener() {
                                @Override
                                public void onClick(int position, Complejo model) {

                                    Intent intentRervaCancha = new Intent(Home.this, ReservaCanchas.class);
                                    intentRervaCancha.putExtra("complejoId", model.getId());
                                    intentRervaCancha.putExtra("fecha", fecha);
                                    intentRervaCancha.putExtra("hora", hora);
                                    intentRervaCancha.putExtra("idUsuario", idUsuario);
                                    startActivity(intentRervaCancha);
                                    //Home.this.finish();
                                }
                            });

                            //recycler.setOnClickListener(onComplejoClickListerner);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Home.this, "Error al obtener los complejos", Toast.LENGTH_SHORT).show();
            }
        });





        requestQueue.add(jsonArrayRequest);
    }


    public void addMarker (GeoPoint center){
        Marker marker = new Marker(mMapView);
        marker.setPosition(center);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setIcon(getResources().getDrawable(R.drawable.persona_saludo));

        mMapView.getOverlays().clear();
        mMapView.getOverlays().add(marker);
        mMapView.invalidate();
        marker.setTitle("este so vo");
    }

    public void addMarker (GeoPoint center, String nombreEstadio, String cantCanchas, String id){
        Marker marker = new Marker(mMapView);
        marker.setPosition(center);
        marker.setId(id);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setIcon(getResources().getDrawable(R.drawable.bs_location));
        // mMapView.getOverlays().clear();
        mMapView.getOverlays().add(marker);
        mMapView.invalidate();




        //marker.getInfoWindow().getView().setBackground(ContextCompat.getDrawable(Home.this, R.drawable.input_button));
        //marker.getInfoWindow().getView().setBackgroundColor(Color.parseColor("#378a1e"));

        marker.setTitle(nombreEstadio + "\n" + cantCanchas +" canchas disponibles");

        marker.setTextLabelForegroundColor(16);
        marker.setTextLabelBackgroundColor(16);
        marker.setOnMarkerClickListener(onMarkerClickListener);
    }

    public Marker.OnMarkerClickListener onMarkerClickListener = new Marker.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker, MapView mapView) {
            marker.showInfoWindow();

            mapView.getController().animateTo(marker.getPosition());



           // Toast.makeText(Home.this, "complejo:" + marker.getTitle(), Toast.LENGTH_SHORT).show();
            return false;
        }
    } ;





    public View.OnClickListener onComplejoClickListerner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
           // TextView item = (TextView) v;
           // String nombre = ((TextView) v).getText().toString();
            Intent intentRervaCancha = new Intent(Home.this, ReservaCanchas.class);
            startActivity(intentRervaCancha);
            Home.this.finish();

        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                // Actualizacion de permisos cuando se soliciten
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permiso concedido, se debe refrescar el mapa o actividad
                    this.recreate();

                }

            }
        }
    }

    public void onResume() {
        super.onResume();

    }

    public void onPause(){
        super.onPause();
    }

    public View.OnClickListener editPerfilListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intentPerfil = new Intent(Home.this, PerfilAficionadoActivity.class);
            startActivity(intentPerfil);
            Home.this.finish();
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


/*
    @Override
    public void onLocationChanged(Location location) {
        GeoPoint center = new GeoPoint(location.getLatitude(), location.getLongitude());

        mMapController.animateTo(center);
        addMarker(center);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        if (locationManager != null){
            locationManager.removeUpdates(this);
        }
    }
*/
}
