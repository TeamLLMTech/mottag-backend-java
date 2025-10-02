package br.com.mottag.api.service;

import br.com.mottag.api.dto.MotoRequestDTO;
import br.com.mottag.api.dto.MotoResponseDTO;
import br.com.mottag.api.exception.NotFoundException;
import br.com.mottag.api.mapper.MotoMapper;
import br.com.mottag.api.model.Moto;
import br.com.mottag.api.model.Patio;
import br.com.mottag.api.model.StatusMoto;
import br.com.mottag.api.repository.MotoRepository;
import br.com.mottag.api.repository.PatioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MotoService {
    private final MotoRepository motoRepository;
    private final PatioRepository patioRepository;

    public MotoService(MotoRepository motoRepository, PatioRepository patioRepository) {
        this.motoRepository = motoRepository;
        this.patioRepository = patioRepository;
    }

    @Transactional
    public MotoResponseDTO save(MotoRequestDTO dto) {
        Patio patio = this.patioRepository.findById(dto.getIdPatio())
                .orElseThrow(() -> new EntityNotFoundException("Patio nao encontrado com o id: " + dto.getIdPatio()));

        Moto moto = MotoMapper.fromDTO(dto);
        moto.setPatio(patio);

        Moto saved = this.motoRepository.save(moto);

        return MotoMapper.toDTO(saved);
    }

    public Page<MotoResponseDTO> findAll(
            Pageable pageable,
            Long idPatio,
            StatusMoto status
    ) {
        return this.motoRepository
                .findAllWithFilters(idPatio, status, pageable)
                .map(MotoMapper::toDTO);
    }

    public MotoResponseDTO findById(Long idMoto) {
        MotoResponseDTO moto = this.motoRepository.findById(idMoto)
                .map(MotoMapper::toDTO)
                .orElseThrow(() -> new NotFoundException("Moto nao encontrada com o id: " + idMoto));
        return moto;
    }

    @Transactional
    public MotoResponseDTO update(Long id, MotoRequestDTO dto) {
        // TODO: update patio ref
        Moto moto = this.motoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Moto nao encontrada com o id: " + id));

        Patio patio = this.patioRepository.findById(dto.getIdPatio())
                .orElseThrow(() -> new EntityNotFoundException("Patio nao encontrado com o id: " + dto.getIdPatio()));

        MotoMapper.updateEntityUsingDTO(moto, dto);
        moto.setPatio(patio);

        Moto updated = this.motoRepository.save(moto);

        return MotoMapper.toDTO(updated);
    }

    @Transactional
    public void delete(Long idMoto) {
        if (!this.motoRepository.existsById(idMoto)) {
            throw new NotFoundException("Moto nao encontrada com o id: " + idMoto);
        }
        this.motoRepository.deleteById(idMoto);
    }

        public java.util.List<MotoResponseDTO> findAllNoPage() {
            return this.motoRepository.findAll().stream()
                .map(MotoMapper::toDTO)
                .toList();
        }

}
