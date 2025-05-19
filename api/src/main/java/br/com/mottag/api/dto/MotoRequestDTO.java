package br.com.mottag.api.dto;

import br.com.mottag.api.model.StatusMoto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class MotoRequestDTO {

    @NotBlank(message = "Campo \"modelo\" não pode ser vazio")
    private String modelo;

    @Pattern(regexp = "^([A-Z]{3}[0-9]{4})|([A-Z]{3}[0-9][A-Z][0-9]{2})$", message = "Campo \"placa\" deve seguir o formato \"AAA1234\" ou \"AAA1A34\"")
    @NotBlank(message = "Campo \"placa\" não pode ser vazio")
    private String placa;

    @NotNull(message = "Campo \"status\" é obrigatório")
    private StatusMoto status;

    @NotNull(message = "Campo \"idPatio\" é obrigatório")
    private Long idPatio;

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

    public Long getIdPatio() {
        return idPatio;
    }

    public void setIdPatio(Long idPatio) {
        this.idPatio = idPatio;
    }
}
