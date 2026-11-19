package com.logintrack.logintrack_backend.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimientos")
@Data
public class Movimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();

    @NotBlank(message = "El tipo de movimiento es obligatorio (ENTRADA, SALIDA, TRANSFERENCIA)")
    @Column(nullable = false)
    private String tipoMovimiento;

    @Column(nullable = false)
    private String usuarioResponsable;

    private String bodegaOrigen;

    @NotBlank(message = "La bodega destino es obligatoria")
    @Column(nullable = false)
    private String bodegaDestino;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    @Column(nullable = false)
    private Integer cantidad;

    @NotNull(message = "El ID del producto es obligatorio")
    private Long productoId;
}
