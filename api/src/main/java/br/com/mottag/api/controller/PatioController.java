package br.com.mottag.api.controller;

import br.com.mottag.api.dto.PatioRequestDTO;
import br.com.mottag.api.dto.PatioResponseDTO;
import br.com.mottag.api.dto.common.ApiResponseDTO;
import br.com.mottag.api.dto.common.PaginationDTO;
import br.com.mottag.api.service.PatioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patios")
public class PatioController {

    private final PatioService patioService;

    public PatioController(PatioService patioService) {
        this.patioService = patioService;
    }

    @PostMapping
    public ResponseEntity<PatioResponseDTO> createPatio(@Valid @RequestBody PatioRequestDTO dto) {
        return new ResponseEntity<PatioResponseDTO>(
                this.patioService.save(dto),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<PatioResponseDTO>>> findAllPatio(
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<PatioResponseDTO> patios = this.patioService.findAll(pageable);
        PaginationDTO pagination = PaginationDTO.fromPage(patios);
        return new ResponseEntity<>(
                new ApiResponseDTO<>(patios.getContent(), pagination),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatioResponseDTO> findByIdPatio(@PathVariable Long id) {
        return new ResponseEntity<PatioResponseDTO>(
                this.patioService.findById(id),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatioResponseDTO> updatePatio(@PathVariable Long id, @Valid @RequestBody PatioRequestDTO dto) {
        return new ResponseEntity<PatioResponseDTO>(
                this.patioService.update(id, dto),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatio(@PathVariable Long id) {
        this.patioService.delete(id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}
