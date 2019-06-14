package com.eitor.tcc.appurosatendente;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;

public class SobreActivity extends AppCompatActivity {
    String cor, servico;
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);

        extras = getIntent().getExtras();
        cor = extras.getString("cor");
        servico = extras.getString("servico");

        String title = "Appuros Atendente";
        SpannableString s = new SpannableString(title);
        s.setSpan(new ForegroundColorSpan(Color.parseColor(extras.getString("cor"))), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
        View linha = findViewById(R.id.linha_sobre);
        linha.setBackgroundColor(Color.parseColor(extras.getString("cor")));


        ImageView btnVoltar = findViewById(R.id.btnVoltarSobre);
        btnVoltar.setBackgroundResource(cor.equals("#385eaa") ? R.drawable.my_button_pm : cor.equals("#C62828") ? R.drawable.my_button_bomb : R.drawable.my_button_samu);

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SobreActivity.this, ChamadasActivity.class);
                i.putExtra("cor", cor);
                i.putExtra("servico", servico);
                startActivity(i);
            }
        });
    }
}

