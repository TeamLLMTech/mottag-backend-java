package br.com.mottag.api.dto;

import java.util.List;

public class PatioResponseDTO {

    private Long idPatio;
    private String nome;
    private String layout;
    private String endereco;
    private List<MotoResponseDTO> motos;

    public Long getIdPatio() {
        return idPatio;
    }

    public void setIdPatio(Long idPatio) {
        this.idPatio = idPatio;
    }

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

    public List<MotoResponseDTO> getMotos() {
        return motos;
    }

    public void setMotos(List<MotoResponseDTO> motos) {
        this.motos = motos;
    }
}
