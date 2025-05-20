package br.com.mottag.api.mapper;

import br.com.mottag.api.dto.MotoRequestDTO;
import br.com.mottag.api.dto.MotoResponseDTO;
import br.com.mottag.api.model.Moto;

public class MotoMapper {
    public static MotoResponseDTO toDTO(Moto moto) {
        MotoResponseDTO dto = new MotoResponseDTO();
        dto.setIdMoto(moto.getIdMoto());
        dto.setModelo(moto.getModelo());
        dto.setPlaca(moto.getPlaca());
        dto.setStatus(moto.getStatus());
        dto.setIdPatio(moto.getPatio().getIdPatio());

        return dto;
    }

    public static Moto fromDTO(MotoRequestDTO dto) {
        Moto moto = new Moto();
        moto.setModelo(dto.getModelo());
        moto.setPlaca(dto.getPlaca());
        moto.setStatus(dto.getStatus());

        return moto;
    }
}
