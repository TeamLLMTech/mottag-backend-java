package br.com.mottag.api.dto;

import jakarta.validation.constraints.NotBlank;

public class PatioRequestDTO {

    @NotBlank(message = "Campo \"nome\" não pode ser vazio")
    private String nome;
    @NotBlank(message = "Campo \"layout\" não pode ser vazio")
    private String layout;
    @NotBlank(message = "Campo \"endereco\" não pode ser vazio")
    private String endereco;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}
