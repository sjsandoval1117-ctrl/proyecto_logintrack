package com.logintrack.logintrack_backend.Model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "auditorias")
@Data
public class Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tipoOperacion; // INSERT, UPDATE, DELETE

    @Column(nullable = false)
    private String entidadAfectada; // BODEGA, PRODUCTO, MOVIMIENTO

    @Column(nullable = false)
    private String usuarioResponsable;

    @Column(nullable = false)
    private LocalDateTime fechaHora = LocalDateTime.now();

    @Column(columnDefinition = "TEXT")
    private String jsonAntes; // Null en INSERT

    @Column(columnDefinition = "TEXT")
    private String jsonDespues; // Null en DELETE
}
