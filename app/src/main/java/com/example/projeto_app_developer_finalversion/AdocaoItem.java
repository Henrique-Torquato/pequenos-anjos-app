package com.example.projeto_app_developer_finalversion;

public class AdocaoItem {

    private String titulo;
    private String idade;
    private String descricao;
    private int imagem;
    private boolean adotado;

    public AdocaoItem(String titulo, String idade, String descricao, int imagem, boolean adotado) {
        this.titulo = titulo;
        this.idade = idade;
        this.descricao = descricao;
        this.imagem = imagem;
        this.adotado = adotado;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getIdade() {
        return idade;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getImagem() {
        return imagem;
    }

    public boolean isAdotado() {
        return adotado;
    }
}
