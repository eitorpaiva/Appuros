package com.eitor.tcc.appurosatendente;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MapaActivity extends FragmentActivity implements OnMapReadyCallback {
    FirebaseFirestore db;
    String username;
    private GoogleMap mMap;
    private Polyline currentPolyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        username = getIntent().getStringExtra("username");
        mMap = googleMap;

        double lat = Double.parseDouble(getIntent().getStringExtra("gps").split(",")[0]);
        double lng = Double.parseDouble(getIntent().getStringExtra("gps").split(",")[1]);

        String nome = getIntent().getStringExtra("nome");

        // Add a marker in Sydney and move the camera
        LatLng localAtualLegal = new LatLng(lat, lng);

        Button btn = findViewById(R.id.btnFinalizar);

        LatLng origin;
        if (getIntent().getStringExtra("servico").equals("pm")) {
           btn.setBackgroundResource(R.drawable.my_button_pm);
            origin = new LatLng(-18.508505, -54.7570933);
        } else if (getIntent().getStringExtra("servico").equals("bomb")) {
            btn.setBackgroundResource(R.drawable.my_button_bomb);
            origin = new LatLng(-18.5220707, -54.7432085);
        } else {
            btn.setBackgroundResource(R.drawable.my_button_samu);
            origin = new LatLng(-18.5220707, -54.7432085);
        }

        mMap.addMarker(new MarkerOptions().position(origin).title(nome));
        mMap.addMarker(new MarkerOptions().position(localAtualLegal).title("Origem"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origin, 14));


            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DocumentReference docRef = db
                            .collection("usuarios")
                            .document(username);
                    Map<String, Object> map = new HashMap<>();
                    map.put("situacao", FieldValue.delete());
                    map.put("servico", FieldValue.delete());
                    docRef.update(map);
                    Intent i = new Intent(MapaActivity.this, ListaActivity.class);
                    i.putExtra("servico", MapaActivity.this.getIntent().getStringExtra("servico"));
                    startActivity(i);
                }
            });
        }
}
