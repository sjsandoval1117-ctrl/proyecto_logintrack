package com.logintrack.logintrack_backend.Repository;

import com.logintrack.logintrack_backend.Model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByStockLessThan(Integer stock);
}
