package com.eitor.tcc.appurosatendente;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChamadasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setElevation(0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chamadas);

        final TextView atendente = findViewById(R.id.atendente);
        atendente.setText("Atendente: " + GoogleSignIn.getLastSignedInAccount(this).getGivenName());
        String email = GoogleSignIn.getLastSignedInAccount(this).getEmail();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference df;
        df = db.collection("atendentes").document(email.substring(0, email.indexOf("@")));
        df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String id = (String) document.get("id");
                        if(id.equals("samu")){
                            String title = "Appuros Atendente";
                            SpannableString s = new SpannableString(title);
                            s.setSpan(new ForegroundColorSpan(Color.parseColor("#FF6F00")), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            getSupportActionBar().setTitle(s);

                            View linha = (View) findViewById(R.id.linha);
                            linha.setBackgroundColor(Color.parseColor("#FF6F00"));

                            atendente.setTextColor(Color.parseColor("#FF6F00"));

                            ImageView imagem = (ImageView) findViewById(R.id.imgEsperando);
                            imagem.setImageResource(R.drawable.ic_esperando_chamada_samu);

                            Button btn = (Button) findViewById(R.id.voltar);
                            btn.setBackgroundResource(R.drawable.my_button_samu);

                        }
                        if(id.equals("bomb")){
                            String title = "Appuros Atendente";
                            SpannableString s = new SpannableString(title);
                            s.setSpan(new ForegroundColorSpan(Color.parseColor("#C62828")), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            getSupportActionBar().setTitle(s);
                            View linha = (View) findViewById(R.id.linha);
                            linha.setBackgroundColor(Color.parseColor("#C62828"));


                            atendente.setTextColor(Color.parseColor("#C62828"));

                            ImageView imagem = (ImageView) findViewById(R.id.imgEsperando);
                            imagem.setImageResource(R.drawable.ic_esperando_chamada_bombeiro);

                            Button btn = (Button) findViewById(R.id.voltar);
                            btn.setBackgroundResource(R.drawable.my_button_bomb);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finishAffinity();
    }
}

