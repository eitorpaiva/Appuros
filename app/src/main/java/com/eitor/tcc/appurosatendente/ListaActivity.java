package com.eitor.tcc.appurosatendente;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class ListaActivity extends AppCompatActivity {
    String servico;
    ListView lista;
    List<Usuario> usuarios;
    CollectionReference cr;
    FirebaseFirestore db;
    DocumentReference docRef;
    TextView nomeLista;
    View l1;
    View l2;
    View l3;
    AlertDialog carregando;
    List<String> usernames;
    Button historico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        usernames = new ArrayList<>();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setElevation(0);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        historico = findViewById(R.id.ir_para_historico);



        nomeLista = findViewById(R.id.nome_lista);
        l1 = findViewById(R.id.linha_lista);
        l2 = findViewById(R.id.linha_lista2);
        l3 = findViewById(R.id.linha_lista3);

        servico = getIntent().getStringExtra("servico");

        usuarios = new ArrayList<>();

        lista = findViewById(R.id.lista_usuarios);

        db = FirebaseFirestore.getInstance();

        cr = db.collection("usuarios");

        cr.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                pqp();
            }
        });

        carregando = new AlertDialog.Builder(this)
                .setTitle("Aguarde")
                .setMessage("Carregando...")
                .show();


    }

    void pqp() {
        usuarios.clear();
        cr.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> documentos = task.getResult().getDocuments();
                    int x = 0;
                    usuarios.clear();
                    for (DocumentSnapshot i : documentos) {
                        if (i.contains("servico")) {
                            if(servico.equals("samu")){
                                String title = "Appuros Atendente";
                                SpannableString s = new SpannableString(title);
                                s.setSpan(new ForegroundColorSpan(Color.parseColor("#FF6F00")), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                getSupportActionBar().setTitle(s);
                                historico.setBackgroundResource(R.drawable.my_button_samu);
                                l1.setBackgroundColor(Color.parseColor("#FF6F00"));


                            }
                            if(servico.equals("bomb")){
                                String title = "Appuros Atendente";
                                SpannableString s = new SpannableString(title);
                                s.setSpan(new ForegroundColorSpan(Color.parseColor("#C62828")), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                getSupportActionBar().setTitle(s);
                                historico.setBackgroundResource(R.drawable.my_button_bomb);
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
                                usernames.add(i.getId());
                                usuarios.add(u);
                            } else {
                                ++x;
                            }
                        } else {
                            ++x;
                        }
                    }
                    if (x >= documentos.size()) {
                        Intent intent = new Intent(ListaActivity.this, ChamadasActivity.class);
                        intent.putExtra("servico", servico);
                        intent.putExtra("nome", getIntent().getStringExtra("nome"));
                        startActivity(intent);
                        ListaActivity.this.finish();
                    }
                    Log.i("x", Integer.toString(x));

                    historico.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ListaActivity.this, HistoricoActivity.class);
                            intent.putExtra("servico", servico);
                            intent.putExtra("cor", getIntent().getStringExtra("cor"));
                            startActivity(intent);
                        }
                    });

                    carregando.hide();
                    carregando.dismiss();

                    UsuarioAdapter adapter = new UsuarioAdapter(usuarios, ListaActivity.this);
                    lista.setAdapter(adapter);
                    lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            if (usuarios.get(position).getGps() == null) {
                                // usuario sem dados

                                AlertDialog alertinha = new AlertDialog.Builder(ListaActivity.this)
                                        .setTitle("Erro")
                                        .setMessage("O usuário não permitiu acesso à localização.")
                                        .setPositiveButton("OK", null)
                                        .show();
                            } else {
                                final String lat = usuarios.get(position).getGps().split(",")[0];
                                final String lon = usuarios.get(position).getGps().split(",")[1];

                                // fazer o desejado

                                final AlertDialog querMesmoAtender = new AlertDialog.Builder(ListaActivity.this)
                                        .setTitle("Deseja atender " + usuarios.get(position).getNome() + "?")
                                        .setMessage("Clique em SIM para confirmar")
                                        .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent i = new Intent(ListaActivity.this, MapaActivity.class);
                                                i.putExtra("gps", lat + "," + lon);
                                                i.putExtra("username", usernames.get(position));
                                                i.putExtra("servico", servico);
                                                i.putExtra("cor", getIntent().getStringExtra("cor"));

                                                i.putExtra("cpf", usuarios.get(position).getCpf());
                                                i.putExtra("nome", usuarios.get(position).getNome());
                                                i.putExtra("sangue", usuarios.get(position).getSangue());
                                                i.putExtra("resmed", usuarios.get(position).getRestricoes());
                                                i.putExtra("endereco", usuarios.get(position).getEndereco());
                                                i.putExtra("telefone", usuarios.get(position).getTelefone());
                                                i.putExtra("emergencia", usuarios.get(position).getContatoEmergencia());

                                                DocumentReference docRef = db
                                                        .collection("usuarios")
                                                        .document(usernames.get(position));
                                                Map<String, Object> map = new HashMap<>();
                                                map.put("nome", usuarios.get(position).getNome() + " (em atendimento)");
                                                docRef.update(map);
                                                startActivity(i);
                                            }
                                        }).setNegativeButton("NÃO", null)
                                        .show();
                            }
                        }
                    });
                }
            }
        });
    }
}
