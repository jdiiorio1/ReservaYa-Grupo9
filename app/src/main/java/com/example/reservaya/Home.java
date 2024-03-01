package com.example.reservaya;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
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

import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Home extends AppCompatActivity {

    private int idUsuario;

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private TextView tvCalendario;
    private TextView tvHorario;
    private EditText tvDistancia;
    private MapView         mMapView;
    private MapController   mMapController;

    private LocationManager locationManager;
    private Location locationGPS = new Location("");

    private RequestQueue requestQueue;
    private LinearLayout parentLayout;
    // Default map zoom level:
    private int MAP_DEFAULT_ZOOM = 14;
    // Default map Latitude:
    private double MAP_DEFAULT_LATITUDE = -34.90445;
    // Default map Longitude:
    private double MAP_DEFAULT_LONGITUDE = -57.92529;
    private int distanciaInt = 100;


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
            } else if (id == R.id.bt_historial_aficionado) {
                overridePendingTransition(R.anim.slide_out_izq, R.anim.slide_in_der);

                Log.i("logReserva", "ingreso a pantalla de historial para el usuairo de id" + idUsuario);
                Intent intentHistorialAficionado = new Intent(Home.this, HistorialReservasAficionadoActivity.class);
                intentHistorialAficionado.putExtra("idUsuario", idUsuario);
                startActivity(intentHistorialAficionado);

                //finish();
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
        tvDistancia = findViewById(R.id.tvdistancia);


        /*
        * Inicializo los valores de los filtros
         */
        // Get Current Date
     /*   final Calendar c = Calendar.getInstance();
        int mAnio = c.get(Calendar.YEAR);
        int mMes = c.get(Calendar.MONTH);
        int mSemana = c.get(Calendar.WEEK_OF_YEAR);
        int mDia = c.get(Calendar.DAY_OF_MONTH);
        int mHora;
        mHora = c.get(Calendar.HOUR_OF_DAY);

        tvCalendario.setText(mAnio + "-" + (mMes + 1) + "-" + mDia);
        tvHorario.setText(mHora);
        Log.i("logReserva", "la distancia sin setear es" + tvDistancia.getText().toString());
*/



        /*
         * Creo los listener para la seleccion de fecha y horario
         * */
        tvCalendario.setOnClickListener(calendarioOnClickListener);
        tvHorario.setOnClickListener(horarioOnClickListener);

        tvDistancia.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i("logReserva", "entro en el listener de distancia" );
                // consulto a la base de datos para obtener los complejos y agregar los marcadores en el mapa
                mMapView.getOverlays().clear();
                mMapView.clearFocus();

                GeoPoint center = new GeoPoint(MAP_DEFAULT_LATITUDE,MAP_DEFAULT_LONGITUDE);
                mMapController.animateTo(center);
                Log.i("logReserva", "la distancia sin setear es" + tvDistancia.getText().toString());
                if (tvDistancia.getText().toString().equals("") || tvDistancia.getText().toString().isEmpty()) {
                    distanciaInt = 100;
                } else {
                    distanciaInt = Integer.parseInt(tvDistancia.getText().toString());
                }

                addMarker(center);
                cargarMapaConComplejos(tvCalendario.getText().toString(), tvHorario.getText().toString(), distanciaInt);
            }
        });



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
        cargarMapaConComplejos(tvCalendario.getText().toString(), tvHorario.getText().toString(), distanciaInt);



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

            // consulto a la base de datos para obtener los complejos y agregar los marcadores en el mapa
            mMapView.getOverlays().clear();
            mMapView.clearFocus();

            GeoPoint center = new GeoPoint(MAP_DEFAULT_LATITUDE,MAP_DEFAULT_LONGITUDE);
            mMapController.animateTo(center);
            Log.i("logReserva", "la distancia sin setear es" + tvDistancia.getText().toString());
            if (tvDistancia.getText().toString().equals("") || tvDistancia.getText().toString().isEmpty()) {
                distanciaInt = 100;
            } else {
                distanciaInt = Integer.parseInt(tvDistancia.getText().toString());
            }

            addMarker(center);
            cargarMapaConComplejos(tvCalendario.getText().toString(), tvHorario.getText().toString(), distanciaInt);

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

                            Log.i("logReserva", "la distancia sin setear es" + tvDistancia.getText().toString());
                            if (tvDistancia.getText().toString().equals("") || tvDistancia.getText().toString().isEmpty()) {
                                distanciaInt = 100;
                            } else {
                                distanciaInt = Integer.parseInt(tvDistancia.getText().toString());
                            }

                            addMarker(center);

                            cargarMapaConComplejos(tvCalendario.getText().toString(), tvHorario.getText().toString(), distanciaInt);

                        }
                    }, mHora, mMinuto, false);
            timePickerDialog.show();



        }
    };




    public void cargarMapaConComplejos (String fecha, String hora, int filtroDistancia) {


        requestQueue = Volley.newRequestQueue(this);
        String URL = "http://192.168.1.35/backend/cargarComplejosFiltrados.php?fecha=" + fecha + "&hora=" + hora;
        Log.i("logReserva", URL);
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
                                String calle = object.getString("calle");
                                String numero = object.getString("numero");
                                String cantCanchas = object.getString("cant_canchas");
                                //Toast.makeText(Home.this, "complejo:" + nombre, Toast.LENGTH_SHORT).show();
                                int imagenComplejo = getResources().getIdentifier("complejo_"+idComplejo, "drawable", getPackageName());
                               // items.add(new Complejo(idComplejo, nombre, imagenComplejo, "Calle " + calle + " Nro: " + numero, cantCanchas, ));

                                // Armo la posicion del complejo como Location para medir la distancia con la locacion del GPS
                                Location markerLocation = new Location("");
                                GeoPoint complejoPoint = new GeoPoint(Double.parseDouble(latitud), Double.parseDouble(longitud));


                                markerLocation.setLatitude(complejoPoint.getLatitude());
                                markerLocation.setLongitude(complejoPoint.getLongitude());

                                Float distancia = markerLocation.distanceTo(locationGPS)/1000;
                                Log.i("logReserva", "complejo: " + nombre + " distancia: " + distancia + " km");
                                NumberFormat formatter = new DecimalFormat("0.00");
                                String dist = formatter.format(distancia).toString();

                                // comparo la distancia seleccionada por el usuario con la del complejo
                                // si es menor agrego el marcador en el mapa y la cardview en la vista


                                if ( distancia < filtroDistancia) {

                                    Log.i("logReserva", "la distancia entre el usuario y el complejo es menor al filtro");
                                    items.add(new Complejo(idComplejo, nombre, imagenComplejo, "Calle " + calle + " Nro: " + numero, cantCanchas, dist + " Km.", true));
                                    addMarker(complejoPoint, nombre, cantCanchas, idComplejo);

                                }


                            }

                            // Crear un nuevo adaptador
                            final ComplejosAdapter adapter = new ComplejosAdapter(items);
                            recycler.setAdapter(adapter);

                            // prueba centrar elemento seleccionado
                           // SnapHelper snapHelperComplejos = new LinearSnapHelper();
                           // snapHelperComplejos.attachToRecyclerView(recycler);
                            // fin prueba

                            // evento del item seleccionado



                            int resId = R.anim.layout_animation_rotate_in;
                            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(Home.this, resId);
                            recycler.setLayoutAnimation(animation);
                            adapter.notifyDataSetChanged();
                            adapter.setOnClickListener(new ComplejosAdapter.OnClickListener() {
                                @Override
                                public void onClick(int position, Complejo model) {
                                    Log.i("logReserva", "entro al listener del complejo");
                                    Intent intentRervaCancha = new Intent(Home.this, ReservaCanchasActivity.class);
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

        // Cargo la localizacion para medir la distancia con cada complejo
        locationGPS.setLatitude(marker.getPosition().getLatitude());
        locationGPS.setLongitude(marker.getPosition().getLongitude());
        Log.i("logReserva", "marcador de gps: " + locationGPS.getLatitude() );

        mMapView.getOverlays().clear();
        mMapView.getOverlays().add(marker);
        mMapView.invalidate();
        marker.setTitle("usted esta aqui");

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
            Intent intentRervaCancha = new Intent(Home.this, ReservaCanchasActivity.class);
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
