package br.com.mottag.api.mapper;

import br.com.mottag.api.dto.PatioResponseDTO;
import br.com.mottag.api.model.Patio;

public class PatioMapper {
    public static PatioResponseDTO toDTO(Patio patio) {
        PatioResponseDTO dto = new PatioResponseDTO();
        dto.setIdPatio(patio.getIdPatio());
        dto.setNome(patio.getNome());
        dto.setLayout(patio.getLayout());
        dto.setEndereco(patio.getEndereco());
        dto.setMotos(patio.getMotos().stream().map(MotoMapper::toDTO).toList());
        return dto;
    }

    public static Patio fromDTO(PatioResponseDTO dto) {
        Patio patio = new Patio();
        patio.setNome(dto.getNome());
        patio.setLayout(dto.getLayout());
        patio.setEndereco(dto.getEndereco());
        return patio;
    }
}
