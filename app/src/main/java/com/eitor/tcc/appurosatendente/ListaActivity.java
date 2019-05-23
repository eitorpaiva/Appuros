package com.eitor.tcc.appurosatendente;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListaActivity extends AppCompatActivity {
    String servico;
    ListView lista;
    List<Usuario> usuarios;
    CollectionReference cr;
    FirebaseFirestore db;
    TextView nomeLista;
    View l1;
    View l2;
    View l3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setElevation(0);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        nomeLista = findViewById(R.id.nome_lista);
        l1 = findViewById(R.id.linha_lista);
        l2 = findViewById(R.id.linha_lista2);
        l3 = findViewById(R.id.linha_lista3);

        servico = getIntent().getStringExtra("servico");

        usuarios = new ArrayList<>();

        lista = findViewById(R.id.lista_usuarios);

        db = FirebaseFirestore.getInstance();

        cr = db.collection("usuarios");

        final AlertDialog carregando = new AlertDialog.Builder(this)
                .setTitle("Aguarde")
                .setMessage("Carregando...")
                .show();

        cr.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> documentos = task.getResult().getDocuments();
                    int x = 0;
                    for (DocumentSnapshot i : documentos) {
                        if (i.contains("servico")) {
                            if(servico.equals("samu")){
                                String title = "Appuros Atendente";
                                SpannableString s = new SpannableString(title);
                                s.setSpan(new ForegroundColorSpan(Color.parseColor("#FF6F00")), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                getSupportActionBar().setTitle(s);
                                l1.setBackgroundColor(Color.parseColor("#FF6F00"));


                            }
                            if(servico.equals("bomb")){
                                String title = "Appuros Atendente";
                                SpannableString s = new SpannableString(title);
                                s.setSpan(new ForegroundColorSpan(Color.parseColor("#C62828")), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                getSupportActionBar().setTitle(s);

                                l1.setBackgroundColor(Color.parseColor("#C62828"));

                            }
                            if (i.get("servico").equals(servico)) {
                                Usuario u = new Usuario(
                                        i.get("contatoEmergencia").toString(),
                                        i.get("cpf").toString(),
                                        i.get("endereco").toString(),
                                        i.get("nome").toString(),
                                        i.get("restricoesMedicas").toString(),
                                        i.get("telefone").toString(),
                                        i.get("tipoSanguineo").toString(),
                                        null
                                );
                                if (i.contains("gps")) {
                                    u = new Usuario(
                                            i.get("contatoEmergencia").toString(),
                                            i.get("cpf").toString(),
                                            i.get("endereco").toString(),
                                            i.get("nome").toString(),
                                            i.get("restricoesMedicas").toString(),
                                            i.get("telefone").toString(),
                                            i.get("tipoSanguineo").toString(),
                                            i.get("gps").toString()
                                    );
                                }
                                usuarios.add(u);
                            } else {
                                ++x;
                            }
                        } else {
                            ++x;
                        }
                    }
                    if (x >= documentos.size()) {
                        startActivity(new Intent(ListaActivity.this, ChamadasActivity.class));
                        ListaActivity.this.finish();
                    }
                    Log.i("x", Integer.toString(x));

                    carregando.hide();
                    carregando.dismiss();

                    UsuarioAdapter adapter = new UsuarioAdapter(usuarios, ListaActivity.this);
                    lista.setAdapter(adapter);
                    lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (usuarios.get(position).getGps() == null) {
                                // usuario sem dados
                            } else {
                                String lat = usuarios.get(position).getGps().split(",")[0];
                                String lon = usuarios.get(position).getGps().split(",")[1];

                                // fazer o desejado
                            }
                        }
                    });
                }
            }
        });
    }
}
