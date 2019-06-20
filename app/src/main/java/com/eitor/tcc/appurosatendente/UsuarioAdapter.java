package com.eitor.tcc.appurosatendente;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class UsuarioAdapter extends BaseAdapter {
    private List<Usuario> dados;
    private Context contexto;

    UsuarioAdapter(List<Usuario> dados, Context contexto) {
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

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(contexto)
                    .inflate(R.layout.usuario_item, parent, false);
        }
        Usuario usuario = dados.get(position);


        TextView nome = convertView.findViewById(R.id.nome_lista);
        nome.setText(usuario.getNome());
        nome.setTextColor(Color.parseColor(((Activity) contexto).getIntent().getStringExtra("cor")));

        TextView cpf = convertView.findViewById(R.id.cpf_lista);
        cpf.setText("CPF: " + usuario.getCpf());

        TextView sangue = convertView.findViewById(R.id.sangue_lista);
        sangue.setText("Tipo sanguíneo: " + usuario.getSangue());

        TextView resMed = convertView.findViewById(R.id.restricao_lista);
        resMed.setText("Restrições médicas: " + usuario.getRestricoes());

        TextView endereco = convertView.findViewById(R.id.endereco_lista);
        endereco.setText("Moradia: " + usuario.getEndereco());

        TextView telefone = convertView.findViewById(R.id.contato_lista);
        telefone.setText("Tel.: " + usuario.getTelefone());

        TextView emergencia = convertView.findViewById(R.id.c_emergencia_lista);
        emergencia.setText("Nº emergência: " + usuario.getContatoEmergencia());

        TextView gravidade = convertView.findViewById(R.id.c_gravidade_lista);
        gravidade.setText("Gravidade: " + usuario.getGravidade());


        View l2 = convertView.findViewById(R.id.linha_lista2);
        l2.setBackgroundColor(Color.parseColor(((Activity) contexto).getIntent().getStringExtra("cor")));

        View l3 = convertView.findViewById(R.id.linha_lista3);
        l3.setBackgroundColor(Color.parseColor(((Activity) contexto).getIntent().getStringExtra("cor")));
        return convertView;
    }
}
