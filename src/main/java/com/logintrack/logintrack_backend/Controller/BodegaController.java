
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

    @GetMapping
    public List<Bodega> listarBodegas() {
        return bodegaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bodega> obtenerBodega(@PathVariable Long id) {
        return bodegaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Bodega> crearBodega(@Valid @RequestBody Bodega bodega) {
        Bodega nuevaBodega = bodegaRepository.save(bodega);
        registrarAuditoria("CREAR_BODEGA", "Bodega creada: " + nuevaBodega.getNombre());
        return ResponseEntity.ok(nuevaBodega);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bodega> actualizarBodega(@PathVariable Long id, @Valid @RequestBody Bodega detallesBodega) {
        return bodegaRepository.findById(id).map(bodega -> {
            bodega.setNombre(detallesBodega.getNombre());
            bodega.setUbicacion(detallesBodega.getUbicacion());
            bodega.setCapacidad(detallesBodega.getCapacidad());
            Bodega actualizada = bodegaRepository.save(bodega);
            registrarAuditoria("ACTUALIZAR_BODEGA", "Bodega actualizada ID: " + id);
            return ResponseEntity.ok(actualizada);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarBodega(@PathVariable Long id) {
        return bodegaRepository.findById(id).map(bodega -> {
            bodegaRepository.delete(bodega);
            registrarAuditoria("ELIMINAR_BODEGA", "Bodega eliminada ID: " + id);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    private void registrarAuditoria(String accion, String detalle) {
        String usuario = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "SISTEMA";
        auditoriaService.registrarAccion(usuario, accion, detalle);
    }
}