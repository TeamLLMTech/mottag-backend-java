package br.com.mottag.api.dto;

import jakarta.validation.constraints.Pattern;

public class MotoRequestDTO {
    @Pattern(regexp = "^([A-Z]{3}[0-9]{4})|([A-Z]{3}[0-9][A-Z][0-9]{2})$", message = "")
    private String placa;
}
