package br.com.mottag.api.mapper;

import br.com.mottag.api.dto.UsuarioRequestDTO;
import br.com.mottag.api.dto.UsuarioResponseDTO;
import br.com.mottag.api.model.Usuario;

public class UsuarioMapper {
    public static UsuarioResponseDTO toDTO(Usuario usuario) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setPerfil(usuario.getPerfil());
        dto.setDataCadastro(usuario.getDataCadastro());
        return dto;
    }

    public static Usuario fromDTO(UsuarioRequestDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());
        usuario.setPerfil(dto.getPerfil());
        usuario.setDataCadastro(dto.getDataCadastro());
        return usuario;
    }

    public static void updateEntityUsingDTO(Usuario usuario, UsuarioRequestDTO dto) {
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());
        usuario.setPerfil(dto.getPerfil());
        usuario.setDataCadastro(dto.getDataCadastro());
    }
}
