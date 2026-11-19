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
    private String tipoOperacion;

    @Column(nullable = false)
    private String entidadAfectada;

    @Column(nullable = false)
    private String usuarioResponsable;

    @Column(nullable = false)
    private LocalDateTime fechaHora = LocalDateTime.now();

    @Column(columnDefinition = "TEXT")
    private String jsonAntes;

    @Column(columnDefinition = "TEXT")
    private String jsonDespues;
}
