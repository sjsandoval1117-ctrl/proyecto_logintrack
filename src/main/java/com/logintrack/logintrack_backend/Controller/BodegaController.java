package com.logintrack.logintrack_backend.Controller;

import com.logintrack.logintrack_backend.Model.Bodega;
import com.logintrack.logintrack_backend.Repository.BodegaRepository;
import com.logintrack.logintrack_backend.Service.AuditoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bodegas")
public class BodegaController {

    @Autowired
    private BodegaRepository bodegaRepository;

    @Autowired
    private AuditoriaService auditoriaService;

    private String getUsuarioActual() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @GetMapping
    public List<Bodega> listar() {
        return bodegaRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Bodega> crear(@Valid @RequestBody Bodega bodega) {
        Bodega guardada = bodegaRepository.save(bodega);
        auditoriaService.registrarAuditoria("INSERT", "BODEGA", getUsuarioActual(), null, guardada);
        return ResponseEntity.ok(guardada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bodega> actualizar(@PathVariable Long id, @Valid @RequestBody Bodega detalles) {
        Bodega bodegaAntes = bodegaRepository.findById(id).orElseThrow(() -> new RuntimeException("Bodega no encontrada"));
        
        Bodega antesCopia = new Bodega();
        antesCopia.setId(bodegaAntes.getId());
        antesCopia.setNombre(bodegaAntes.getNombre());
        antesCopia.setUbicacion(bodegaAntes.getUbicacion());
        antesCopia.setCapacidad(bodegaAntes.getCapacidad());
        antesCopia.setEncargado(bodegaAntes.getEncargado());

        bodegaAntes.setNombre(detalles.getNombre());
        bodegaAntes.setUbicacion(detalles.getUbicacion());
        bodegaAntes.setCapacidad(detalles.getCapacidad());
        bodegaAntes.setEncargado(detalles.getEncargado());

        Bodega actualizada = bodegaRepository.save(bodegaAntes);
        auditoriaService.registrarAuditoria("UPDATE", "BODEGA", getUsuarioActual(), antesCopia, actualizada);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Bodega bodega = bodegaRepository.findById(id).orElseThrow(() -> new RuntimeException("Bodega no encontrada"));
        bodegaRepository.delete(bodega);
        auditoriaService.registrarAuditoria("DELETE", "BODEGA", getUsuarioActual(), bodega, null);
        return ResponseEntity.ok("Bodega eliminada con éxito");
    }
}
