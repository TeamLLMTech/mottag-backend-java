package br.com.mottag.api.service;

import br.com.mottag.api.dto.PatioRequestDTO;
import br.com.mottag.api.dto.PatioResponseDTO;
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
    // @CachePut(value = "patios")
    // @CacheEvict(value = "patios", allEntries = true)
    public PatioResponseDTO save(PatioRequestDTO dto) {
        Patio saved = this.patioRepository.save(PatioMapper.fromDTO(dto));
        return PatioMapper.toDTO(saved);
    }

    // @Cacheable(value = "patios")
    public Page<PatioResponseDTO> findAll(Pageable pageable) {
        return this.patioRepository.findAll(pageable).map(PatioMapper::toDTO);
    }

    // @Cacheable(value = "patios", key = "#id")
    public PatioResponseDTO findById(Long id) {
        PatioResponseDTO patio = this.patioRepository.findById(id)
                .map(PatioMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Patio nao encontrado com o id: " + id));
        return patio;
    }

    @Transactional
    // CachePut(value = "patios", key = "#result.idPatio")
    public PatioResponseDTO update(Long id, PatioRequestDTO dto) {
        Patio patio = this.patioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patio nao encontrado com o id: " + id));

        PatioMapper.updateEntityUsingDTO(patio, dto);

        Patio updated = this.patioRepository.save(patio);

        return PatioMapper.toDTO(updated);
    }

    @Transactional
    // @CacheEvict(value = "patios", key = "#id")
    public void delete(Long id) {
        if (!this.patioRepository.existsById(id)) {
            throw new EntityNotFoundException("Patio nao encontrado com o id: " + id);
        }
        this.patioRepository.deleteById(id);
        // cleanCacheOfAllPatios();
    }

    // Cache methods
    // @CacheEvict(value = "patios", key = "'all'")
    // public void cleanCacheOfAllPatios() {}

    // @CacheEvict(value = "patios", allEntries = true)
    // public void cleanAllPatiosFromCache() {}

}
