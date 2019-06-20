package com.eitor.tcc.appurosatendente;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ConfigActivity extends AppCompatActivity {

    TextView
            nome, cpf, endereco, telefone, matricula, sangue, patente;
    String cor, servico;
    ImageView foto;
    Bundle extras;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setElevation(0);


        extras = getIntent().getExtras();

        db = FirebaseFirestore.getInstance();
        final String email = GoogleSignIn.getLastSignedInAccount(this).getEmail();
        final Uri fotoURL = GoogleSignIn.getLastSignedInAccount(this).getPhotoUrl();

        cor = extras.getString("cor");

        String title = "Appuros Atendente";
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.parseColor(extras.getString("cor"))), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
        View linha = findViewById(R.id.linha_config);
        linha.setBackgroundColor(Color.parseColor(extras.getString("cor")));


        servico = extras.getString("servico");

        foto = findViewById(R.id.perfil);
        nome = findViewById(R.id.nomeConfig);
        cpf = findViewById(R.id.cpfConfig);
        endereco = findViewById(R.id.enderecoConfig);
        telefone = findViewById(R.id.telefoneConfig);
        matricula = findViewById(R.id.matriculaConfig);
        sangue = findViewById(R.id.sangueConfig);
        patente = findViewById(R.id.patenteConfig);

        db.collection("atendentes").document(email.substring(0, email.indexOf("@"))).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ConnectivityManager cm = (ConnectivityManager) ConfigActivity.this.getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();

                if (isConnected && fotoURL != null) {
                    new DownloadImageTask(foto, ConfigActivity.this).execute(fotoURL.toString());
                    foto.setImageURI(fotoURL);
                } else {
                    foto.setImageResource(R.drawable.ic_autorenew_black_24dp);
                }
                nome.setText("Nome: " + documentSnapshot.get("nome").toString());
                cpf.setText("CPF: " + documentSnapshot.get("cpf").toString());
                patente.setText("Patente: " + documentSnapshot.get("patente").toString());
                endereco.setText("Endereço: " + documentSnapshot.get("endereco").toString());
                telefone.setText("Telefone: " + documentSnapshot.get("telefone").toString());
                matricula.setText("Matrícula: " + documentSnapshot.get("matricula").toString());
                sangue.setText("Tipo Sanguíneo: " + documentSnapshot.get("tipoSanguineo").toString());
            }
        });


        Button btnEditar = findViewById(R.id.btnEditarConfig);
        btnEditar.setBackgroundResource(cor.equals("#385eaa") ? R.drawable.my_button_pm : cor.equals("#C62828") ? R.drawable.my_button_bomb : R.drawable.my_button_samu);
        Log.e("cor", cor);

        Button btnLogout = findViewById(R.id.btnLogoutConfig);
        btnLogout.setBackgroundResource(cor.equals("#385eaa") ? R.drawable.my_button_pm : cor.equals("#C62828") ? R.drawable.my_button_bomb : R.drawable.my_button_samu);

        Button btnApagar = findViewById(R.id.btnApagarConfig);
        btnApagar.setBackgroundResource(cor.equals("#385eaa") ? R.drawable.my_button_pm : cor.equals("#C62828") ? R.drawable.my_button_bomb : R.drawable.my_button_samu);

        ImageView btnVoltar = findViewById(R.id.btnVoltarConfig);
        btnVoltar.setBackgroundResource(cor.equals("#385eaa") ? R.drawable.my_button_pm : cor.equals("#C62828") ? R.drawable.my_button_bomb : R.drawable.my_button_samu);

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference editarDocRef = db
                        .collection("atendentes")
                        .document(email.substring(0, email.indexOf("@")));

                editarDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Intent i;
                        if (servico.equals("samu")) {
                            i = new Intent(ConfigActivity.this, SAMUCadActivity.class);
                        } else if (servico.equals("bomb")) {
                            i = new Intent(ConfigActivity.this, BombCadActivity.class);
                        } else {
                            i = new Intent(ConfigActivity.this, PMCadActivity.class);
                        }
                        i.putExtra("nome", documentSnapshot.get("nome").toString());
                        i.putExtra("cpf", documentSnapshot.get("cpf").toString());
                        i.putExtra("endereco", documentSnapshot.get("endereco").toString());
                        i.putExtra("matricula", documentSnapshot.get("matricula").toString());
                        i.putExtra("telefone", documentSnapshot.get("telefone").toString());
                        startActivity(i);
                    }
                });
            }
        });

        btnApagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ConfigActivity.this)
                        .setMessage("Deseja realmente apagar o seu cadastro?")
                        .setTitle("Apagar Cadastro")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.collection("atendentes")
                                        .document(email.substring(0, email.indexOf("@")))
                                        .delete();
                                fazerLogout();
                            }
                        })
                        .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ConfigActivity.this)
                        .setMessage("Deseja realmente sair?")
                        .setTitle("Fazer Logout")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                fazerLogout();
                            }
                        })
                        .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(ConfigActivity.this, "Obrigado por ficar!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ConfigActivity.this, ChamadasActivity.class);
                i.putExtra("cor", cor);
                i.putExtra("servico", servico);
                startActivity(i);
            }
        });

    }


    private void fazerLogout() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(ConfigActivity.this, MainActivity.class));
                    }
                });
    }
}
