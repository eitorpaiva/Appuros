package com.eitor.tcc.appurosatendente;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;
    final int CODIGO_LOGIN = 467;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final AlertDialog carregando = new AlertDialog.Builder(this)
                .setTitle("Aguarde")
                .setMessage("Carregando...")
                .show();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.edit().putString("corGlobal","red").apply();

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        String ss = sp.getString("corGlobal","");

        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
            GoogleSignInAccount conta = GoogleSignIn.getLastSignedInAccount(this);
            String e = conta.getEmail();
            FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
            DocumentReference docIdRef = rootRef.collection("atendentes").document(e.substring(0, e.indexOf("@")));
            docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            carregando.hide();
                            carregando.dismiss();
                            Intent intent = new Intent(MainActivity.this, ListaActivity.class);
                            intent.putExtra("servico", document.get("id").toString());
                            intent.putExtra("nome", document.get("nome").toString());
                            if (document.get("id").equals("bomb")) {
                                intent.putExtra("cor", "#C62828");
                            } else if (document.get("id").equals("samu")) {
                                intent.putExtra("cor", "#FF6F00");
                            } else {
                                intent.putExtra("cor", "#385eaa");
                            }
                            startActivity(intent);
                        }
                    } else {
                        Log.d("TAG", "Failed with: ", task.getException());
                    }
                }
            });
        } else {
            carregando.hide();
            carregando.dismiss();
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setElevation(0);

        FirebaseApp.initializeApp(this);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (GoogleSignIn.getLastSignedInAccount(this) == null) {
            Button login = findViewById(R.id.sign_in);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult(intent, CODIGO_LOGIN);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

        try {
            GoogleSignInAccount conta = task.getResult(ApiException.class);
            String e = conta.getEmail();
            FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
            DocumentReference docIdRef = rootRef.collection("atendentes").document(e.substring(0, e.indexOf("@")));
            docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Intent i = new Intent(MainActivity.this, ListaActivity.class);
                            i.putExtra("servico", document.get("id").toString());
                            i.putExtra("nome", document.get("nome").toString());
                            if (document.get("id").equals("bomb")) {
                                i.putExtra("cor", "#C62828");
                            } else if (document.get("id").equals("samu")) {
                                i.putExtra("cor", "#FF6F00");
                            } else {
                                i.putExtra("cor", "#385eaa");
                            }
                            startActivity(i);
                        } else {
                            startActivity(new Intent(MainActivity.this, EscolherActivity.class));
                        }
                    } else {
                        Log.d("TAG", "Failed with: ", task.getException());
                    }
                }
            });
        } catch (ApiException e) {
            Log.i("Erro no login", "ApiException encontrada");
        }
    }
}
