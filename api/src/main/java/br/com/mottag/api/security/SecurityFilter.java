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
