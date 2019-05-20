package com.eitor.tcc.appurosatendente;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class PMCadActivity extends AppCompatActivity {
    FirebaseFirestore db;
    TextView
            _nome, _cpf, _endereco, _telefone, _matricula;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pmcad);
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
        patentes.add("Soldado PM Temporário");
        patentes.add("Soldado 2ª Classe");
        patentes.add("Soldado 1ª Classe");
        patentes.add("Cabo");
        patentes.add("Terceiro Tenente");
        patentes.add("Segundo Tenente");
        patentes.add("Primeiro Tenente");
        patentes.add("Subtenente");
        patentes.add("Cadete");
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

        final Spinner tipoSanguineo = findViewById(R.id.spinnerSangues);

        tipoSanguineo.setAdapter(a);

        final Spinner patente = findViewById(R.id.spinnerPatentes);

        patente.setAdapter(b);

        Button salvar = findViewById(R.id.btnCadastro);

        db = FirebaseFirestore.getInstance();

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GoogleSignInAccount conta = GoogleSignIn.getLastSignedInAccount(PMCadActivity.this);
                assert conta != null;
                String
                        id = "pm",
                        nome = _nome.getEditableText().toString(),
                        cpf = _cpf.getEditableText().toString(),
                        endereco = _endereco.getEditableText().toString(),
                        telefone = _telefone.getEditableText().toString(),
                        matricula = _matricula.getEditableText().toString();
                if(CNP.isValidCPF(cpf) && !(nome.isEmpty()) && !(telefone.isEmpty()) && !(endereco.isEmpty())){
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

                    startActivity(new Intent(PMCadActivity.this, ChamadasActivity.class));
                    PMCadActivity.this.finish();

                }if (!(CNP.isValidCPF(cpf))){
                    new AlertDialog.Builder(PMCadActivity.this)
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
                    new AlertDialog.Builder(PMCadActivity.this)
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

