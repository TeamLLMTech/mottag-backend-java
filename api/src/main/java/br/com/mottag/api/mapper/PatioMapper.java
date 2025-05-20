package br.com.mottag.api.mapper;

import br.com.mottag.api.dto.PatioRequestDTO;
import br.com.mottag.api.dto.PatioResponseDTO;
import br.com.mottag.api.model.Moto;
import br.com.mottag.api.model.Patio;

import java.util.ArrayList;
import java.util.List;

public class PatioMapper {
    public static PatioResponseDTO toDTO(Patio patio) {
        PatioResponseDTO dto = new PatioResponseDTO();
        dto.setIdPatio(patio.getIdPatio());
        dto.setNome(patio.getNome());
        dto.setLayout(patio.getLayout());
        dto.setEndereco(patio.getEndereco());
        return dto;
    }

    public static Patio fromDTO(PatioRequestDTO dto) {
        Patio patio = new Patio();
        patio.setNome(dto.getNome());
        patio.setLayout(dto.getLayout());
        patio.setEndereco(dto.getEndereco());
        return patio;
    }

    public static void updateEntityUsingDTO(Patio patio, PatioRequestDTO dto) {
        if (dto == null || patio == null) return;

        patio.setNome(dto.getNome());
        patio.setLayout(dto.getLayout());
        patio.setEndereco(dto.getEndereco());
    }
}
