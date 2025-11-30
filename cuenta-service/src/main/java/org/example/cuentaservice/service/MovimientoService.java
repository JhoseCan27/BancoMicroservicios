package org.example.cuentaservice.service;

import lombok.RequiredArgsConstructor;
import org.example.cuentaservice.exception.InsufficientBalanceException;
import org.example.cuentaservice.exception.ResourceNotFoundException;
import org.example.cuentaservice.model.Cuenta;
import org.example.cuentaservice.model.Movimiento;
import org.example.cuentaservice.repository.CuentaRepository;
import org.example.cuentaservice.repository.MovimientoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;

    @Transactional
    public Movimiento registrarMovimiento(String numeroCuenta, String tipoMovimiento, BigDecimal valor) {
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada: " + numeroCuenta));

        if (!cuenta.getEstado()) {
            throw new IllegalStateException("La cuenta no está activa");
        }

        BigDecimal saldoActual = obtenerSaldoActual(cuenta.getId());
        BigDecimal nuevoSaldo;

        if ("DEPOSITO".equalsIgnoreCase(tipoMovimiento) || "Depósito".equalsIgnoreCase(tipoMovimiento)) {
            nuevoSaldo = saldoActual.add(valor.abs());
        } else if ("RETIRO".equalsIgnoreCase(tipoMovimiento) || "Retiro".equalsIgnoreCase(tipoMovimiento)) {
            if (saldoActual.compareTo(valor.abs()) < 0) {
                throw new InsufficientBalanceException("Saldo no disponible");
            }
            nuevoSaldo = saldoActual.subtract(valor.abs());
            valor = valor.abs().negate();
        } else {
            throw new IllegalArgumentException("Tipo de movimiento inválido: " + tipoMovimiento);
        }

        Movimiento movimiento = new Movimiento();
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setTipoMovimiento(tipoMovimiento);
        movimiento.setValor(valor);
        movimiento.setSaldo(nuevoSaldo);
        movimiento.setCuenta(cuenta);

        return movimientoRepository.save(movimiento);
    }

    @Transactional(readOnly = true)
    public BigDecimal obtenerSaldoActual(Long cuentaId) {
        return movimientoRepository.findTopByCuentaIdOrderByFechaDesc(cuentaId)
                .map(Movimiento::getSaldo)
                .orElseGet(() -> {
                    Cuenta cuenta = cuentaRepository.findById(cuentaId)
                            .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada"));
                    return cuenta.getSaldoInicial();
                });
    }

    @Transactional(readOnly = true)
    public List<Movimiento> obtenerMovimientos(String numeroCuenta, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada: " + numeroCuenta));

        return movimientoRepository.findByCuentaIdAndFechaBetweenOrderByFechaDesc(
                cuenta.getId(), fechaInicio, fechaFin);
    }
}