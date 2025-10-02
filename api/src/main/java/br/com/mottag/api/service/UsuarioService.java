package br.com.mottag.api.service;

import br.com.mottag.api.dto.UsuarioLoginRequestDTO;
import br.com.mottag.api.dto.UsuarioLoginResponseDTO;
import br.com.mottag.api.dto.UsuarioRequestDTO;
import br.com.mottag.api.dto.UsuarioResponseDTO;
import br.com.mottag.api.exception.NotFoundException;
import br.com.mottag.api.exception.UnauthorizedException;
import br.com.mottag.api.mapper.UsuarioMapper;
import br.com.mottag.api.model.Usuario;
import br.com.mottag.api.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final TokenService tokenService;

    public UsuarioService(UsuarioRepository usuarioRepository, TokenService tokenService) {
        this.usuarioRepository = usuarioRepository;
        this.tokenService = tokenService;
    }

    public UsuarioLoginResponseDTO login(UsuarioLoginRequestDTO loginDTO) {
        Usuario usuario = usuarioRepository.findByEmail(loginDTO.getEmail());
        if (usuario != null && new BCryptPasswordEncoder().matches(loginDTO.getSenha(), usuario.getSenha())) {
            return new UsuarioLoginResponseDTO(
                tokenService.generateToken(loginDTO.getEmail(), usuario.getIdUsuario()),
                usuario.getIdUsuario()
            );
        }
        throw new UnauthorizedException("Usuário ou senha incorretos");
    }

    @Transactional
    public UsuarioResponseDTO save(UsuarioRequestDTO dto) {
        String encryptedPwd = new BCryptPasswordEncoder().encode(dto.getSenha());
        dto.setSenha(encryptedPwd);
        Usuario saved = usuarioRepository.save(UsuarioMapper.fromDTO(dto));
        return UsuarioMapper.toDTO(saved);
    }

    public Page<UsuarioResponseDTO> findAll(Pageable pageable) {
        return usuarioRepository.findAll(pageable).map(UsuarioMapper::toDTO);
    }

    public UsuarioResponseDTO findById(Long id) {
        return usuarioRepository.findById(id)
            .map(UsuarioMapper::toDTO)
            .orElseThrow(() -> new NotFoundException("Usuário não encontrado com o ID: " + id));
    }

    @Transactional
    public UsuarioResponseDTO update(Long id, UsuarioRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Usuário não encontrado com o ID: " + id));

        String encryptedPwd = new BCryptPasswordEncoder().encode(dto.getSenha());
        dto.setSenha(encryptedPwd);

        UsuarioMapper.updateEntityUsingDTO(usuario, dto);
        return UsuarioMapper.toDTO(usuarioRepository.save(usuario));
    }

    @Transactional
    public void delete(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new NotFoundException("Usuário não encontrado com o ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }
}
