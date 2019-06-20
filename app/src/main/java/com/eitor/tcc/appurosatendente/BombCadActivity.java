package com.eitor.tcc.appurosatendente;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BombCadActivity extends AppCompatActivity {
    FirebaseFirestore db;
    TextView
            _nome, _cpf, _endereco, _telefone, _matricula;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String title = "Appuros Atendente";
        Bundle extras = getIntent().getExtras();
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#C62828")), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bomb_cad);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setElevation(0);

        ArrayList<String> sangues = new ArrayList<>();
        sangues.add("A+");
        sangues.add("A-");
        sangues.add("B+");
        sangues.add("B-");
        sangues.add("AB+");
        sangues.add("AB-");
        sangues.add("O+");
        sangues.add("O-");

        ArrayAdapter<String> a;
        a = new ArrayAdapter<>(this, R.layout.spinner_item, sangues);
        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayList<String> patentes = new ArrayList<>();
        patentes.add("Aluno CFP");
        patentes.add("Soldado");
        patentes.add("Cabo");
        patentes.add("Sargento");
        patentes.add("Subtenente");
        patentes.add("Aluno-Oficial");
        patentes.add("Tenente");
        patentes.add("Capitão");
        patentes.add("Major");
        patentes.add("Tenente Coronel");
        patentes.add("Coronel");

        ArrayAdapter<String> b;
        b = new ArrayAdapter<>(this, R.layout.spinner_item, patentes);
        b.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        _nome = findViewById(R.id.nome);
        _cpf = findViewById(R.id.cpf);
        _endereco = findViewById(R.id.endereco);
        _telefone = findViewById(R.id.telefone);
        _matricula = findViewById(R.id.matricula);

        if (getIntent().getExtras() != null) {
            _nome.setText(getIntent().getStringExtra("nome"));
            _cpf.setText(getIntent().getStringExtra("cpf"));
            _endereco.setText(getIntent().getStringExtra("endereco"));
            _telefone.setText(getIntent().getStringExtra("telefone"));
            _matricula.setText(getIntent().getStringExtra("matricula"));
        }

        final Spinner tipoSanguineo = findViewById(R.id.spinnerSangues);

        tipoSanguineo.setAdapter(a);

        final Spinner patente = findViewById(R.id.spinnerPatentes);

        patente.setAdapter(b);

        Button salvar = findViewById(R.id.btnCadastro);

        db = FirebaseFirestore.getInstance();

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GoogleSignInAccount conta = GoogleSignIn.getLastSignedInAccount(BombCadActivity.this);
                assert conta != null;
                String
                        id = "bomb",
                        nome = _nome.getEditableText().toString(),
                        cpf = _cpf.getEditableText().toString(),
                        endereco = _endereco.getEditableText().toString(),
                        telefone = _telefone.getEditableText().toString(),
                        matricula = _matricula.getEditableText().toString();
                if (CNP.isValidCPF(cpf) && !(nome.isEmpty()) && !(telefone.isEmpty()) && !(endereco.isEmpty()) && !(matricula.isEmpty())) {
                    db = FirebaseFirestore.getInstance();
                    email = conta.getEmail();
                    Map<String, String> dados = new HashMap<>();
                    dados.put("id", id);
                    dados.put("nome", nome);
                    dados.put("patente", patente.getSelectedItem().toString());
                    dados.put("cpf", cpf);
                    dados.put("endereco", endereco);
                    dados.put("telefone", telefone);
                    dados.put("tipoSanguineo", tipoSanguineo.getSelectedItem().toString());
                    dados.put("matricula", matricula);

                    if (db.collection("atendentes").document(email.substring(0, email.indexOf("@"))).set(dados).isSuccessful())
                        Log.i("sucesso", "deu");
                    else
                        Log.i("falha", "fodeu");

                    startActivity(
                            new Intent(BombCadActivity.this, ListaActivity.class)
                                    .putExtra("nome", nome)
                                    .putExtra("servico", id)
                    );                    BombCadActivity.this.finish();

                }if (!(CNP.isValidCPF(cpf))){
                    new AlertDialog.Builder(BombCadActivity.this)
                            .setTitle("Erro no Cadastro")
                            .setMessage("CPF inválido.")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                }

                if ((nome.isEmpty()) || (telefone.isEmpty()) || (endereco.isEmpty())
                        || (matricula.isEmpty())){
                    new AlertDialog.Builder(BombCadActivity.this)
                            .setTitle("Erro no Cadastro")
                            .setMessage("Preencha todos os campos!")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                }
            }

        });
    }
}
