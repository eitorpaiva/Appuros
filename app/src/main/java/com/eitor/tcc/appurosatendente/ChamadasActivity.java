package com.eitor.tcc.appurosatendente;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChamadasActivity extends AppCompatActivity {
    FirebaseFirestore db;
    CollectionReference colRef;
    String nome, servico;
    Button historico;

    public static void tintMenuIcon(Context context, MenuItem item, @ColorRes int color) {
        Drawable normalDrawable = item.getIcon();
        Drawable wrapDrawable = DrawableCompat.wrap(normalDrawable);
        DrawableCompat.setTint(wrapDrawable, context.getResources().getColor(color));

        item.setIcon(wrapDrawable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        colRef = db.collection("usuarios");


        servico = getIntent().getStringExtra("servico");

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setElevation(0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chamadas);

        final String cor1 = "#FF6F00";
        final String cor2 = "#C62828";

        historico = findViewById(R.id.ir_para_historico_chamadas);

        final TextView atendente = findViewById(R.id.atendente);
        nome = getIntent().getStringExtra("nome");

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        String email = GoogleSignIn.getLastSignedInAccount(this).getEmail();
        db.collection("atendentes")
                .document(email.substring(0, email.indexOf("@")))
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                atendente.setText("Atendente: " + documentSnapshot.get("nome").toString().split(" ")[0]);
            }
        });


        DocumentReference df;
        df = db.collection("atendentes").document(email.substring(0, email.indexOf("@")));
        df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    final DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String id = (String) document.get("id");
                        if(id.equals("samu")){
                            atendente.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(ChamadasActivity.this, SAMUCadActivity.class);
                                    i.putExtra("nome", document.get("nome").toString());
                                    i.putExtra("cpf", document.get("cpf").toString());
                                    i.putExtra("endereco", document.get("endereco").toString());
                                    i.putExtra("matricula", document.get("matricula").toString());
                                    i.putExtra("telefone", document.get("telefone").toString());
                                    startActivity(i);
                                }
                            });

                            String title = "Appuros Atendente";
                            SpannableString s = new SpannableString(title);
                            s.setSpan(new ForegroundColorSpan(Color.parseColor("#FF6F00")), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            getSupportActionBar().setTitle(s);

                            View linha = findViewById(R.id.linha);
                            linha.setBackgroundColor(Color.parseColor("#FF6F00"));

                            atendente.setTextColor(Color.parseColor("#FF6F00"));

                            ImageView imagem = findViewById(R.id.imgEsperando);
                            imagem.setImageResource(R.drawable.ic_esperando_chamada_samu);

                            Button btn = findViewById(R.id.voltar);
                            btn.setBackgroundResource(R.drawable.my_button_samu);
                            historico.setBackgroundResource(R.drawable.my_button_samu);

                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(ChamadasActivity.this, ListaActivity.class);
                                    intent.putExtra("servico", "samu");
                                    intent.putExtra("nome", nome);
                                    intent.putExtra("cor", document.get("id").equals("bomb") ? "#C62828" : document.get("id").equals("samu") ? "#FF6F00" : "#385eaa");
                                    startActivity(intent);

                                }
                            });

                            historico.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(ChamadasActivity.this, HistoricoActivity.class);
                                    intent.putExtra("servico", "samu");
                                    intent.putExtra("cor", document.get("id").equals("bomb") ? "#C62828" : document.get("id").equals("samu") ? "#FF6F00" : "#385eaa");
                                    startActivity(intent);
                                }
                            });

                        }
                        if(id.equals("bomb")){
                            String title = "Appuros Atendente";
                            SpannableString s = new SpannableString(title);
                            s.setSpan(new ForegroundColorSpan(Color.parseColor("#C62828")), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            getSupportActionBar().setTitle(s);
                            View linha = findViewById(R.id.linha);
                            linha.setBackgroundColor(Color.parseColor("#C62828"));

                            atendente.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(ChamadasActivity.this, BombCadActivity.class);
                                    i.putExtra("nome", document.get("nome").toString());
                                    i.putExtra("cpf", document.get("cpf").toString());
                                    i.putExtra("endereco", document.get("endereco").toString());
                                    i.putExtra("matricula", document.get("matricula").toString());
                                    i.putExtra("telefone", document.get("telefone").toString());
                                    startActivity(i);
                                }
                            });


                            atendente.setTextColor(Color.parseColor("#C62828"));

                            ImageView imagem = findViewById(R.id.imgEsperando);
                            imagem.setImageResource(R.drawable.ic_esperando_chamada_bombeiro);

                            Button btn = findViewById(R.id.voltar);
                            btn.setBackgroundResource(R.drawable.my_button_bomb);
                            historico.setBackgroundResource(R.drawable.my_button_bomb);

                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(ChamadasActivity.this, ListaActivity.class);
                                    intent.putExtra("servico", "bomb");
                                    intent.putExtra("nome", nome);
                                    intent.putExtra("cor", document.get("id").equals("bomb") ? "#C62828" : document.get("id").equals("samu") ? "#FF6F00" : "#385eaa");
                                    startActivity(intent);

                                }
                            });

                            historico.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(ChamadasActivity.this, HistoricoActivity.class);
                                    intent.putExtra("servico", "bomb");
                                    intent.putExtra("cor", document.get("id").equals("bomb") ? "#C62828" : document.get("id").equals("samu") ? "#FF6F00" : "#385eaa");
                                    startActivity(intent);
                                }
                            });
                        }
                        if(id.equals("pm")){

                            atendente.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(ChamadasActivity.this, PMCadActivity.class);
                                    i.putExtra("nome", document.get("nome").toString());
                                    i.putExtra("cpf", document.get("cpf").toString());
                                    i.putExtra("endereco", document.get("endereco").toString());
                                    i.putExtra("matricula", document.get("matricula").toString());
                                    i.putExtra("telefone", document.get("telefone").toString());
                                    startActivity(i);
                                }
                            });

                            Button btn = findViewById(R.id.voltar);
                            btn.setBackgroundResource(R.drawable.my_button_pm);
                            historico.setBackgroundResource(R.drawable.my_button_pm);

                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(ChamadasActivity.this, ListaActivity.class);
                                    intent.putExtra("servico", "pm");
                                    intent.putExtra("nome", nome);
                                    intent.putExtra("cor", document.get("id").equals("bomb") ? "#C62828" : document.get("id").equals("samu") ? "#FF6F00" : "#385eaa");
                                    startActivity(intent);

                                }
                            });

                            historico.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(ChamadasActivity.this, HistoricoActivity.class);
                                    intent.putExtra("servico", "pm");
                                    intent.putExtra("cor", document.get("id").equals("bomb") ? "#C62828" : document.get("id").equals("samu") ? "#FF6F00" : "#385eaa");
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        if (servico.equals("bomb")) {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_info_bomb_24dp));
        } else if (servico.equals("samu")) {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_info_samu_24dp));
        } else if (servico.equals("pm")) {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_info_pm_24dp));
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mdica:
                Toast.makeText(this, "Para alterar acessar as opções de seu cadastro, clique no ícone de três pontinhos ao lado.", Toast.LENGTH_LONG)
                        .show();
                break;
            case R.id.msobre:
                new AlertDialog.Builder(this)
                        .setMessage("Appuros\nCriado por Eitor Paiva e Mariana Dias.")
                        .setTitle("Sobre:")
                        .show();
                break;
            case R.id.mconfig:
                Intent i = new Intent(ChamadasActivity.this, ConfigActivity.class);
                i.putExtra("servico", servico);
                i.putExtra("cor", servico.equals("bomb") ? "#C62828" : servico.equals("samu") ? "#FF6F00" : "#385eaa");
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finishAffinity();
    }
}

