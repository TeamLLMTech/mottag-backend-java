package br.com.mottag.api.model;

import jakarta.persistence.*;

@Entity
public class Moto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMoto;
    private String modelo;
    @Column(unique = true)
    private String placa;
    private StatusMoto status;

    public Long getIdMoto() {
        return idMoto;
    }

    public void setIdMoto(Long idMoto) {
        this.idMoto = idMoto;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public StatusMoto getStatus() {
        return status;
    }

    public void setStatus(StatusMoto status) {
        this.status = status;
    }
}
