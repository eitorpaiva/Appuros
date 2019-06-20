package com.eitor.tcc.appurosatendente;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
public class MapaActivity extends FragmentActivity implements OnMapReadyCallback {
    FirebaseFirestore db;
    String username;
    private GoogleMap mMap;

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

        LatLng localAtualLegal = new LatLng(lat, lng);

        Button btn = findViewById(R.id.btnFinalizar);

        if (getIntent().getStringExtra("servico").equals("pm")) {
            btn.setBackgroundResource(R.drawable.my_button_pm);
        } else if (getIntent().getStringExtra("servico").equals("bomb")) {
            btn.setBackgroundResource(R.drawable.my_button_bomb);
        } else {
            btn.setBackgroundResource(R.drawable.my_button_samu);
        }
        mMap.addMarker(new MarkerOptions().position(localAtualLegal).title(nome));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localAtualLegal, 14));


        btn.setOnClickListener(v -> {
            CollectionReference historico = db
                    .collection("historico");
            Map<String, Object> entrada = new HashMap<>();

            entrada.put("nome", getIntent().getStringExtra("nome"));
            entrada.put("cpf", getIntent().getStringExtra("cpf"));
            entrada.put("sangue", getIntent().getStringExtra("sangue"));
            entrada.put("resmed", getIntent().getStringExtra("resmed"));
            entrada.put("endereco", getIntent().getStringExtra("endereco"));
            entrada.put("telefone", getIntent().getStringExtra("telefone"));
            entrada.put("emergencia", getIntent().getStringExtra("emergencia"));
            entrada.put("gravidade", getIntent().getStringExtra("gravidade"));
            entrada.put("servico", getIntent().getStringExtra("servico"));

            historico.add(entrada).addOnSuccessListener(documentReference -> {
                DocumentReference docRef = db
                        .collection("usuarios")
                        .document(username);
                Map<String, Object> map = new HashMap<>();
                map.put("situacao", FieldValue.delete());
                map.put("servico", FieldValue.delete());
                map.put("gravidade", FieldValue.delete());
                docRef.update(map);

                DocumentReference usuario = db.collection("usuarios").document(getIntent().getStringExtra("username"));
                Map<String, Object> map2 = new HashMap<>();
                map2.put("nome", getIntent().getStringExtra("nome"));
                usuario.update(map2).addOnSuccessListener(aVoid -> {
                    Intent i = new Intent(MapaActivity.this, ListaActivity.class);
                    i.putExtra("servico", MapaActivity.this.getIntent().getStringExtra("servico"));
                    i.putExtra("nome", getIntent().getStringExtra("nome"));
                    i.putExtra("cor", getIntent().getStringExtra("cor"));
                    startActivity(i);
                });
            });
        });
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        AlertDialog alertinha = new AlertDialog.Builder(MapaActivity.this)
                .setTitle("Acesso Negado!")
                .setMessage("Você não pode voltar até finalizar o atendimento.")
                .setPositiveButton("OK", null)
                .show();
    }
}
