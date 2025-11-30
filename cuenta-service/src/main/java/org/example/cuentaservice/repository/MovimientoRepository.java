package org.example.cuentaservice.repository;

import org.example.cuentaservice.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    List<Movimiento> findByCuentaIdAndFechaBetweenOrderByFechaDesc(
            Long cuentaId, LocalDateTime fechaInicio, LocalDateTime fechaFin);

    Optional<Movimiento> findTopByCuentaIdOrderByFechaDesc(Long cuentaId);
}
