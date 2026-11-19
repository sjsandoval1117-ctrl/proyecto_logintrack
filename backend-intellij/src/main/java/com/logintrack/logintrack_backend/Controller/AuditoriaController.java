package com.logintrack.logintrack_backend.Controller;

import com.logintrack.logintrack_backend.Model.Auditoria;
import com.logintrack.logintrack_backend.Repository.AuditoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auditorias")
public class AuditoriaController {

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    @GetMapping
    public List<Auditoria> listarTodas() {
        return auditoriaRepository.findAll();
    }

    @GetMapping("/usuario/{username}")
    public List<Auditoria> listarPorUsuario(@PathVariable String username) {
        return auditoriaRepository.findByUsuarioResponsable(username);
    }

    @GetMapping("/operacion/{tipo}")
    public List<Auditoria> listarPorOperacion(@PathVariable String tipo) {
        return auditoriaRepository.findByTipoOperacion(tipo.toUpperCase());
    }
}
