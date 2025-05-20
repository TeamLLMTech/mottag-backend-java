package br.com.mottag.api.controller;

import br.com.mottag.api.dto.MotoRequestDTO;
import br.com.mottag.api.dto.MotoResponseDTO;
import br.com.mottag.api.service.MotoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/motos")
public class MotoController {

    private final MotoService motoService;

    public MotoController(MotoService motoService) {
        this.motoService = motoService;
    }

    @PostMapping
    public ResponseEntity<MotoResponseDTO> createMoto(@Valid @RequestBody MotoRequestDTO dto) {
        return new ResponseEntity<MotoResponseDTO>(
                this.motoService.save(dto),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<Page<MotoResponseDTO>> findAllMoto(
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return new ResponseEntity<Page<MotoResponseDTO>>(
                this.motoService.findAll(pageable),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<MotoResponseDTO> findByIdMoto(@PathVariable Long id) {
        return new ResponseEntity<MotoResponseDTO>(
                this.motoService.findById(id),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<MotoResponseDTO> updateMoto(@PathVariable Long id, @Valid @RequestBody MotoRequestDTO dto) {
        return new ResponseEntity<MotoResponseDTO>(
                this.motoService.update(id, dto),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMoto(@PathVariable Long id) {
        this.motoService.delete(id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}
