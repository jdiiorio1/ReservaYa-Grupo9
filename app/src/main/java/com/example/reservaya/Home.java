package com.example.reservaya;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import org.osmdroid.views.overlay.mylocation.DirectedLocationOverlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    private Button edit_perfil;
    private MapView         mMapView;
    private MapController   mMapController;
    private MyLocationNewOverlay mMyLocationOverlay;

    private LocationManager locationManager;
    private CompassOverlay compassOverlay;
    private MyLocationNewOverlay mylocationOverlay;
    private DirectedLocationOverlay locationOverlay;


    ArrayList<OverlayItem> overlayItemArray;
    // Default map zoom level:
    private int MAP_DEFAULT_ZOOM = 16;

    // Default map Latitude:
    private double MAP_DEFAULT_LATITUDE = -34.90445;

    // Default map Longitude:
    private double MAP_DEFAULT_LONGITUDE = -57.92529;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //setTitle("Comencemos");

        /*
         * Asocio las instancias a la interfaz
         * */
        edit_perfil = findViewById(R.id.bt_perfil);

        /*
         * Creo los listener de los elementos clickeables
         * */
        edit_perfil.setOnClickListener(editPerfilListener);


        if (Build.VERSION.SDK_INT >= 21) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                String[] permisos = {Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permisos, 1);
            }
        }
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


        addMarker(new GeoPoint(-34.90816, -57.91680), "Estadio 2");
        addMarker(new GeoPoint(-34.91525, -57.93554), "Nueva Cancha");
        addMarker(new GeoPoint(-34.91552, -57.94464), "Estadio Nuevo");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);

        /* grade no mapa
        LatLonGridlineOverlay2 overlay = new LatLonGridlineOverlay2();
        osm.getOverlays().add(overlay);
        */

        mMapView.setMapListener(new MapListener() {
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
    public void addMarker (GeoPoint center){
        Marker marker = new Marker(mMapView);
        marker.setPosition(center);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setIcon(getResources().getDrawable(R.drawable.bs_my_location));
        mMapView.getOverlays().clear();
        mMapView.getOverlays().add(marker);
        mMapView.invalidate();
        marker.setTitle("este so vo");
    }

    public void addMarker (GeoPoint center, String nombreEstadio){
        Marker marker = new Marker(mMapView);
        marker.setPosition(center);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setIcon(getResources().getDrawable(R.drawable.bs_location));
        // mMapView.getOverlays().clear();
        mMapView.getOverlays().add(marker);
        mMapView.invalidate();
        marker.setTitle(nombreEstadio);
    }



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
