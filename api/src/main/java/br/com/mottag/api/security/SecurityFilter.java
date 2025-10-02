package br.com.mottag.api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.mottag.api.model.Usuario;
import br.com.mottag.api.repository.UsuarioRepository;
import br.com.mottag.api.service.TokenService;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;
    @Autowired
    public SecurityFilter(TokenService tokenService, UsuarioRepository usuarioRepository) {
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);
        if (token != null) {
            var email = tokenService.validateToken(token);
            UserDetails userDetails = this.usuarioRepository.findByEmail(email);
            var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Access control based on perfil
            if (userDetails instanceof Usuario usuario) {
                String perfil = usuario.getPerfil();
                String path = request.getRequestURI();
                if ("OPERADOR".equals(perfil)) {
                    // Only allow /motos routes
                    if (!path.startsWith("/motos")) {
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        response.getWriter().write("Acesso negado: OPERADOR apenas pode acessar rotas de motos.");
                        return;
                    }
                }
                // GESTOR can access all routes (no restriction)
            }
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var header = request.getHeader("Authorization"); // "Bearer jdhkjsdhfjghsdfghjsdhfgshdfgh"
        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        }
        return header.replace("Bearer ", ""); //"jdhkjsdhfjghsdfghjsdhfgshdfgh"
    }
}
