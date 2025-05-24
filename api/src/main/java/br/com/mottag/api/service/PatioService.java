package br.com.mottag.api.service;

import br.com.mottag.api.dto.PatioRequestDTO;
import br.com.mottag.api.dto.PatioResponseDTO;
import br.com.mottag.api.exception.NotFoundException;
import br.com.mottag.api.mapper.PatioMapper;
import br.com.mottag.api.model.Patio;
import br.com.mottag.api.repository.PatioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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

    @Transactional
    @CachePut(value = "patios", key = "#result.idPatio")
    @CacheEvict(value = "patios", allEntries = true)
    public PatioResponseDTO save(PatioRequestDTO dto) {
        Patio saved = this.patioRepository.save(PatioMapper.fromDTO(dto));

        cleanCacheOfAllPatios();

        return PatioMapper.toDTO(saved);
    }

    // @Cacheable(value = "patios", key = "'all'")
    public Page<PatioResponseDTO> findAll(Pageable pageable) {
        return this.patioRepository.findAll(pageable).map(PatioMapper::toDTO);
    }

    @Cacheable(value = "patios", key = "#idPatio")
    public PatioResponseDTO findById(Long idPatio) {
        PatioResponseDTO patio = this.patioRepository.findById(idPatio)
                .map(PatioMapper::toDTO)
                .orElseThrow(() -> new NotFoundException("Patio nao encontrado com o id: " + idPatio));
        return patio;
    }

    @Transactional
    @CachePut(value = "patios", key = "#result.idPatio")
    public PatioResponseDTO update(Long id, PatioRequestDTO dto) {
        Patio patio = this.patioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Patio nao encontrado com o id: " + id));

        PatioMapper.updateEntityUsingDTO(patio, dto);

        Patio updated = this.patioRepository.save(patio);

        cleanCacheOfAllPatios();

        return PatioMapper.toDTO(updated);
    }

    @Transactional
    @CacheEvict(value = "patios", key = "#idPatio")
    public void delete(Long idPatio) {
        if (!this.patioRepository.existsById(idPatio)) {
            throw new NotFoundException("Patio nao encontrado com o id: " + idPatio);
        }
        this.patioRepository.deleteById(idPatio);

        cleanAllPatiosFromCache();
    }

    // Cache methods
    @CacheEvict(value = "patios", key = "'all'")
    public void cleanCacheOfAllPatios() {}

    @CacheEvict(value = "patios", allEntries = true)
    public void cleanAllPatiosFromCache() {}

}
