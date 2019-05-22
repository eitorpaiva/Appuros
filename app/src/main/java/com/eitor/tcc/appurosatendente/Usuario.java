package com.eitor.tcc.appurosatendente;

public class Usuario {
    private String
            contatoEmergencia;
    private String cpf;
    private String endereco;
    private String nome;
    private String restricoes;
    private String telefone;
    private String sangue;

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    private String gps;

    public Usuario(String contatoEmergencia, String cpf, String endereco, String nome, String restricoes, String telefone, String sangue, String gps) {
        this.contatoEmergencia = contatoEmergencia;
        this.cpf = cpf;
        this.endereco = endereco;
        this.nome = nome;
        this.restricoes = restricoes;
        this.telefone = telefone;
        this.sangue = sangue;
        this.gps = gps;
    }

    public String getContatoEmergencia() {
        return contatoEmergencia;
    }

    public void setContatoEmergencia(String contatoEmergencia) {
        this.contatoEmergencia = contatoEmergencia;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRestricoes() {
        return restricoes;
    }

    public void setRestricoes(String restricoes) {
        this.restricoes = restricoes;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSangue() {
        return sangue;
    }

    public void setSangue(String sangue) {
        this.sangue = sangue;
    }
}
