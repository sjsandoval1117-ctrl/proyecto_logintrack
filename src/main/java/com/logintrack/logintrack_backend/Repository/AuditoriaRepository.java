package com.logintrack.logintrack_backend.Repository;

import com.logintrack.logintrack_backend.Model.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {
    List<Auditoria> findByUsuarioResponsable(String usuarioResponsable);
    List<Auditoria> findByTipoOperacion(String tipoOperacion);
}
