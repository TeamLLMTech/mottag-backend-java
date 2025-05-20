package br.com.mottag.api.service;

import br.com.mottag.api.dto.MotoRequestDTO;
import br.com.mottag.api.dto.MotoResponseDTO;
import br.com.mottag.api.mapper.MotoMapper;
import br.com.mottag.api.model.Moto;
import br.com.mottag.api.model.Patio;
import br.com.mottag.api.repository.MotoRepository;
import br.com.mottag.api.repository.PatioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MotoService {
    private final MotoRepository motoRepository;
    private final PatioRepository patioRepository;

    public MotoService(MotoRepository motoRepository, PatioRepository patioRepository) {
        this.motoRepository = motoRepository;
        this.patioRepository = patioRepository;
    }

    public MotoResponseDTO save(MotoRequestDTO dto) {
        Patio patio = this.patioRepository.findById(dto.getIdPatio())
                .orElseThrow(() -> new EntityNotFoundException("Patio nao encontrado com o id: " + dto.getIdPatio()));

        Moto moto = MotoMapper.fromDTO(dto);
        moto.setPatio(patio);

        Moto saved = this.motoRepository.save(moto);
        return MotoMapper.toDTO(saved);
    }

    public Page<MotoResponseDTO> findAll(Pageable pageable) {
        return this.motoRepository.findAll(pageable).map(MotoMapper::toDTO);
    }

    public MotoResponseDTO findById(Long id) {
        MotoResponseDTO moto = this.motoRepository.findById(id)
                .map(MotoMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Moto nao encontrada com o id: " + id));
        return moto;
    }

    public MotoResponseDTO update(Long id, MotoRequestDTO dto) {
        Moto moto = this.motoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Moto nao encontrada com o id: " + id));

        MotoMapper.updateEntityUsingDTO(moto, dto);

        Moto updated = this.motoRepository.save(moto);

        return MotoMapper.toDTO(updated);
    }

    public void delete(Long id) {
        if (!this.motoRepository.existsById(id)) {
            throw new EntityNotFoundException("Moto nao encontrada com o id: " + id);
        }
        this.motoRepository.deleteById(id);
    }

}
