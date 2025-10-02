package br.com.mottag.api.service;

import br.com.mottag.api.dto.PatioRequestDTO;
import br.com.mottag.api.dto.PatioResponseDTO;
import br.com.mottag.api.exception.NotFoundException;
import br.com.mottag.api.mapper.PatioMapper;
import br.com.mottag.api.model.Patio;
import br.com.mottag.api.repository.PatioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PatioService {
    private final PatioRepository patioRepository;

    public PatioService(PatioRepository patioRepository) {
        this.patioRepository = patioRepository;
    }

    public java.util.List<PatioResponseDTO> findAllNoPage() {
        return this.patioRepository.findAll().stream()
            .map(PatioMapper::toDTO)
            .toList();
    }

    @Transactional
    public PatioResponseDTO save(PatioRequestDTO dto) {
        Patio saved = this.patioRepository.save(PatioMapper.fromDTO(dto));

        return PatioMapper.toDTO(saved);
    }

    public Page<PatioResponseDTO> findAll(Pageable pageable) {
        return this.patioRepository.findAll(pageable).map(PatioMapper::toDTO);
    }

    public PatioResponseDTO findById(Long idPatio) {
        PatioResponseDTO patio = this.patioRepository.findById(idPatio)
                .map(PatioMapper::toDTO)
                .orElseThrow(() -> new NotFoundException("Patio nao encontrado com o id: " + idPatio));
        return patio;
    }

    @Transactional
    public PatioResponseDTO update(Long id, PatioRequestDTO dto) {
        Patio patio = this.patioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Patio nao encontrado com o id: " + id));

        PatioMapper.updateEntityUsingDTO(patio, dto);

        Patio updated = this.patioRepository.save(patio);

        return PatioMapper.toDTO(updated);
    }

    @Transactional
    public void delete(Long idPatio) {
        if (!this.patioRepository.existsById(idPatio)) {
            throw new NotFoundException("Patio nao encontrado com o id: " + idPatio);
        }
        this.patioRepository.deleteById(idPatio);
    }
}
