package com.logintrack.logintrack_backend.Controller;

import com.logintrack.logintrack_backend.Model.Usuario;
import com.logintrack.logintrack_backend.Repository.UsuarioRepository;
import com.logintrack.logintrack_backend.Security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> registrarUsuario(@Valid @RequestBody Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setStatus("PENDIENTE");
        if (usuario.getRole() == null) usuario.setRole("EMPLEADO");
        Usuario nuevo = usuarioRepository.save(usuario);
        return ResponseEntity.ok(nuevo);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            String token = jwtUtil.generateToken(request.getUsername());
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (Exception e) {
            System.out.println(">>> ERROR EN LOGIN: " + e.getClass().getName() + " - " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(401).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/admin/solicitudes")
    public List<Usuario> listarSolicitudesPendientes() {
        return usuarioRepository.findByStatus("PENDIENTE");
    }

    @PostMapping("/admin/aprobar/{id}")
    public ResponseEntity<?> aprobarUsuario(@PathVariable Long id) {
        Usuario u = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        u.setStatus("APROBADO");
        usuarioRepository.save(u);
        return ResponseEntity.ok("Usuario aprobado con éxito");
    }

    @PostMapping("/admin/rechazar/{id}")
    public ResponseEntity<?> rechazarUsuario(@PathVariable Long id) {
        Usuario u = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        u.setStatus("RECHAZADO");
        usuarioRepository.save(u);
        return ResponseEntity.ok("Usuario rechazado");
    }
}

class AuthRequest {
    private String username;
    private String password;
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

class AuthResponse {
    private final String jwt;
    public AuthResponse(String jwt) { this.jwt = jwt; }
    public String getJwt() { return jwt; }
}