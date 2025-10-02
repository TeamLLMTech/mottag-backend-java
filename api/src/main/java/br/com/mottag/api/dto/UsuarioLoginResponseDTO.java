package br.com.mottag.api.dto;

public class UsuarioLoginResponseDTO {

  private String token;
  private Long idUsuario;

  public UsuarioLoginResponseDTO() {
  }

  public UsuarioLoginResponseDTO(String token, Long idUsuario) {
    this.token = token;
    this.idUsuario = idUsuario;
  }

  public String getToken() {
    return token;
  }
  public void setToken(String token) {
    this.token = token;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }
}
