package com.logintrack.logintrack_backend.Security;

import com.logintrack.logintrack_backend.Model.Usuario;
import com.logintrack.logintrack_backend.Repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwt);
            } catch (Exception e) {
                System.out.println(">>> Error extrayendo username del JWT: " + e.getMessage());
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println(">>> Usuario extraído del token: " + username);
            try {
                boolean esValido = jwtUtil.validateToken(jwt, username);
                System.out.println(">>> ¿Es el token válido?: " + esValido);

                if (esValido) {
                    Usuario u = usuarioRepository.findByUsername(username).orElse(null);
                    System.out.println(">>> Usuario encontrado en BD: " + (u != null ? u.getUsername() : "null"));

                    if (u != null && "APROBADO".equals(u.getStatus())) {
                        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + u.getRole());
                        UserDetails userDetails = new User(u.getUsername(), u.getPassword(), Collections.singletonList(authority));

                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                        System.out.println(">>> ¡Autenticación establecida con éxito en el SecurityContext!");
                    } else {
                        System.out.println(">>> El usuario no existe en la BD o su estado no es APROBADO.");
                    }
                }
            } catch (Exception e) {
                System.out.println(">>> Error validando token o cargando usuario: " + e.getMessage());
            }
        }

        chain.doFilter(request, response);
    }
}