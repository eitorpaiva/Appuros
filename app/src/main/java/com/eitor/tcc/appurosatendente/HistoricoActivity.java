package com.eitor.tcc.appurosatendente;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HistoricoActivity extends AppCompatActivity {
    CollectionReference collectionHistorico;
    List<Usuario> listaHistorico;
    FirebaseFirestore db;
    View l1;
    ListView historico;
    String servico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setElevation(0);

        db = FirebaseFirestore.getInstance();

        l1 = findViewById(R.id.linha_historico);
        historico = findViewById(R.id.lista_historico);
        servico = getIntent().getStringExtra("servico");
        listaHistorico = new ArrayList<>();

        if (servico.equals("samu")) {
            String title = "Appuros Atendente";
            SpannableString s = new SpannableString(title);
            s.setSpan(new ForegroundColorSpan(Color.parseColor("#FF6F00")), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            getSupportActionBar().setTitle(s);
            l1.setBackgroundColor(Color.parseColor("#FF6F00"));
        } else if (servico.equals("bomb")) {
            String title = "Appuros Atendente";
            SpannableString s = new SpannableString(title);
            s.setSpan(new ForegroundColorSpan(Color.parseColor("#C62828")), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            getSupportActionBar().setTitle(s);
            l1.setBackgroundColor(Color.parseColor("#C62828"));
        } else {
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        collectionHistorico = db.collection("historico");
        collectionHistorico.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> entradas = queryDocumentSnapshots.getDocuments();

                for (DocumentSnapshot i : entradas) {
                    if (i.get("servico").equals(servico)) {
                        listaHistorico.add(new Usuario(
                                i.get("emergencia").toString(),
                                i.get("cpf").toString(),
                                i.get("endereco").toString(),
                                i.get("nome").toString(),
                                i.get("matricula").toString(),
                                i.get("telefone").toString(),
                                i.get("sangue").toString()
                        ));
                    }
                }

                UsuarioAdapter adapter = new UsuarioAdapter(listaHistorico, HistoricoActivity.this);

                historico.setAdapter(adapter);
            }
        });
    }
}
