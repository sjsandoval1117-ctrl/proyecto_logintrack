package com.logintrack.logintrack_backend.Controller;

import com.logintrack.logintrack_backend.Model.Producto;
import com.logintrack.logintrack_backend.Repository.ProductoRepository;
import com.logintrack.logintrack_backend.Service.AuditoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private AuditoriaService auditoriaService;

    private String getUsuarioActual() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @GetMapping
    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    @GetMapping("/stock-bajo")
    public List<Producto> stockBajo() {
        return productoRepository.findByStockLessThan(10);
    }

    @PostMapping
    public ResponseEntity<Producto> crear(@Valid @RequestBody Producto producto) {
        Producto guardado = productoRepository.save(producto);
        auditoriaService.registrarAuditoria("INSERT", "PRODUCTO", getUsuarioActual(), null, guardado);
        return ResponseEntity.ok(guardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @Valid @RequestBody Producto productoDetalles) {
        Producto productoAntes = productoRepository.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        Producto antesCopia = new Producto();
        antesCopia.setId(productoAntes.getId());
        antesCopia.setNombre(productoAntes.getNombre());
        antesCopia.setCategoria(productoAntes.getCategoria());
        antesCopia.setStock(productoAntes.getStock());
        antesCopia.setPrecio(productoAntes.getPrecio());

        productoAntes.setNombre(productoDetalles.getNombre());
        productoAntes.setCategoria(productoDetalles.getCategoria());
        productoAntes.setStock(productoDetalles.getStock());
        productoAntes.setPrecio(productoDetalles.getPrecio());

        Producto actualizado = productoRepository.save(productoAntes);
        auditoriaService.registrarAuditoria("UPDATE", "PRODUCTO", getUsuarioActual(), antesCopia, actualizado);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Producto producto = productoRepository.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        productoRepository.delete(producto);
        auditoriaService.registrarAuditoria("DELETE", "PRODUCTO", getUsuarioActual(), producto, null);
        return ResponseEntity.ok("Producto eliminado con éxito");
    }
}
