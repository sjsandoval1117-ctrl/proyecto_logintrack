package com.logintrack.logintrack_backend.Controller;

import com.logintrack.logintrack_backend.Model.Movimiento;
import com.logintrack.logintrack_backend.Repository.MovimientoRepository;
import com.logintrack.logintrack_backend.Service.AuditoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
public class MovimientoController {

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private AuditoriaService auditoriaService;

    private String getUsuarioActual() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @GetMapping
    public List<Movimiento> listar() {
        return movimientoRepository.findAll();
    }

    @GetMapping("/fechas")
    public List<Movimiento> listarPorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return movimientoRepository.findByFechaBetween(inicio, fin);
    }

    @PostMapping
    public ResponseEntity<Movimiento> crear(@Valid @RequestBody Movimiento movimiento) {
        movimiento.setUsuarioResponsable(getUsuarioActual());
        Movimiento guardado = movimientoRepository.save(movimiento);
        auditoriaService.registrarAuditoria("INSERT", "MOVIMIENTO", getUsuarioActual(), null, guardado);
        return ResponseEntity.ok(guardado);
    }
}
