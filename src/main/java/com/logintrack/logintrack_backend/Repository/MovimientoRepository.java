package com.logintrack.logintrack_backend.Repository;

import com.logintrack.logintrack_backend.Model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    List<Movimiento> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);
}
