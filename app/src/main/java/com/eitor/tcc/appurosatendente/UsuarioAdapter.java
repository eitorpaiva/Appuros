package com.eitor.tcc.appurosatendente;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class UsuarioAdapter extends BaseAdapter {
    private List<Usuario> dados;
    private Context contexto;

    public UsuarioAdapter(List<Usuario> dados, Context contexto) {
        this.dados = dados;
        this.contexto = contexto;
    }

    @Override
    public int getCount() {
        return dados.size();
    }

    @Override
    public Object getItem(int position) {
        return dados.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(contexto)
                    .inflate(R.layout.usuario_item, parent, false);
        }
        Usuario usuario = dados.get(position);


        TextView nome = convertView.findViewById(R.id.nome_lista);
        nome.setText(usuario.getNome());

        TextView sangue = convertView.findViewById(R.id.sangue_lista);
        sangue.setText(usuario.getSangue());

        TextView resMed = convertView.findViewById(R.id.restricao_lista);
        resMed.setText(usuario.getRestricoes());

        TextView endereco = convertView.findViewById(R.id.endereco_lista);
        endereco.setText(usuario.getEndereco());

        TextView telefone = convertView.findViewById(R.id.contato_lista);
        telefone.setText("Tel.: " + usuario.getTelefone());

        TextView emergencia = convertView.findViewById(R.id.c_emergencia_lista);
        emergencia.setText("Nº emergência: " + usuario.getContatoEmergencia());
        return convertView;
    }
}
