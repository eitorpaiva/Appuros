package com.eitor.tcc.appurosatendente;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class EscolherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setElevation(0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolher);

    }


        public void abrirSAMU(View view) {
            Intent i = new Intent(EscolherActivity.this,SAMUCadActivity.class);
            startActivity(i);
        }

        public void abrirBomb(View view) {
            Intent i = new Intent(EscolherActivity.this,BombCadActivity.class);
            startActivity(i);
        }

        public void abrirPM(View view) {
            Intent i = new Intent(EscolherActivity.this,PMCadActivity.class);
            startActivity(i);
        }

}
