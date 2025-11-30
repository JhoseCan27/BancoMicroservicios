package org.example.cuentaservice.service;

import org.example.cuentaservice.exception.InsufficientBalanceException;
import org.example.cuentaservice.exception.ResourceNotFoundException;
import org.example.cuentaservice.model.Cuenta;
import org.example.cuentaservice.model.Movimiento;
import org.example.cuentaservice.repository.CuentaRepository;
import org.example.cuentaservice.repository.MovimientoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovimientoServiceTest {

    @Mock
    private MovimientoRepository movimientoRepository;

    @Mock
    private CuentaRepository cuentaRepository;

    @InjectMocks
    private MovimientoService movimientoService;

    private Cuenta cuenta;
    private Movimiento movimientoAnterior;

    @BeforeEach
    void setUp() {
        cuenta = new Cuenta();
        cuenta.setId(1L);
        cuenta.setNumeroCuenta("478758");
        cuenta.setTipoCuenta("Ahorros");
        cuenta.setSaldoInicial(new BigDecimal("2000.00"));
        cuenta.setEstado(true);
        cuenta.setClienteId("jose-lema-001");

        movimientoAnterior = new Movimiento();
        movimientoAnterior.setId(1L);
        movimientoAnterior.setFecha(LocalDateTime.now().minusDays(1));
        movimientoAnterior.setTipoMovimiento("Depósito");
        movimientoAnterior.setValor(new BigDecimal("500.00"));
        movimientoAnterior.setSaldo(new BigDecimal("2500.00"));
        movimientoAnterior.setCuenta(cuenta);
    }

    @Test
    void testRegistrarDepositoExitoso() {
        when(cuentaRepository.findByNumeroCuenta("478758")).thenReturn(Optional.of(cuenta));
        when(movimientoRepository.findTopByCuentaIdOrderByFechaDesc(1L))
                .thenReturn(Optional.of(movimientoAnterior));

        Movimiento nuevoMovimiento = new Movimiento();
        nuevoMovimiento.setId(2L);
        nuevoMovimiento.setFecha(LocalDateTime.now());
        nuevoMovimiento.setTipoMovimiento("Depósito");
        nuevoMovimiento.setValor(new BigDecimal("600.00"));
        nuevoMovimiento.setSaldo(new BigDecimal("3100.00"));
        nuevoMovimiento.setCuenta(cuenta);

        when(movimientoRepository.save(any(Movimiento.class))).thenReturn(nuevoMovimiento);

        Movimiento resultado = movimientoService.registrarMovimiento(
                "478758",
                "Depósito",
                new BigDecimal("600.00")
        );

        assertNotNull(resultado);
        assertEquals(new BigDecimal("3100.00"), resultado.getSaldo());
        assertEquals(new BigDecimal("600.00"), resultado.getValor());
        assertEquals("Depósito", resultado.getTipoMovimiento());
        verify(movimientoRepository, times(1)).save(any(Movimiento.class));
    }

    @Test
    void testRegistrarRetiroExitoso() {
        when(cuentaRepository.findByNumeroCuenta("478758")).thenReturn(Optional.of(cuenta));
        when(movimientoRepository.findTopByCuentaIdOrderByFechaDesc(1L))
                .thenReturn(Optional.of(movimientoAnterior));

        Movimiento nuevoMovimiento = new Movimiento();
        nuevoMovimiento.setId(2L);
        nuevoMovimiento.setFecha(LocalDateTime.now());
        nuevoMovimiento.setTipoMovimiento("Retiro");
        nuevoMovimiento.setValor(new BigDecimal("-575.00"));
        nuevoMovimiento.setSaldo(new BigDecimal("1925.00"));
        nuevoMovimiento.setCuenta(cuenta);

        when(movimientoRepository.save(any(Movimiento.class))).thenReturn(nuevoMovimiento);


    Movimiento resultado = movimientoService.registrarMovimiento(
            "478758",
            "Retiro",
            new BigDecimal("575.00"));

    assertNotNull(resultado);
    assertEquals(new BigDecimal("1925.00"), resultado.getSaldo());
    assertTrue(resultado.getValor().compareTo(BigDecimal.ZERO) < 0);
    assertEquals("Retiro", resultado.getTipoMovimiento());
    verify(movimientoRepository, times(1)).save(any(Movimiento.class));
            }

    @Test
    void testRegistrarRetiroError() {
        when(cuentaRepository.findByNumeroCuenta("478758")).thenReturn(Optional.of(cuenta));
        when(movimientoRepository.findTopByCuentaIdOrderByFechaDesc(1L))
                .thenReturn(Optional.of(movimientoAnterior));

        assertThrows(InsufficientBalanceException.class, () -> {
            movimientoService.registrarMovimiento(
                    "478758",
                    "Retiro",
                    new BigDecimal("3000.00") // Más del saldo disponible
            );
        });

        verify(movimientoRepository, never()).save(any(Movimiento.class));
    }

    @Test
    void testRegistrarMovimientoCuentaNoEncontrada() {
        when(cuentaRepository.findByNumeroCuenta("999999")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            movimientoService.registrarMovimiento(
                    "999999",
                    "Depósito",
                    new BigDecimal("100.00")
            );
        });

        verify(movimientoRepository, never()).save(any(Movimiento.class));
    }

    @Test
    void testRegistrarMovimientoCuentaInactiva() {
        cuenta.setEstado(false);
        when(cuentaRepository.findByNumeroCuenta("478758")).thenReturn(Optional.of(cuenta));

        assertThrows(IllegalStateException.class, () -> {
            movimientoService.registrarMovimiento(
                    "478758",
                    "Depósito",
                    new BigDecimal("100.00")
            );
        });

        verify(movimientoRepository, never()).save(any(Movimiento.class));
    }

    @Test
    void testObtenerSaldoActualConMovimientos() {
        when(movimientoRepository.findTopByCuentaIdOrderByFechaDesc(1L))
                .thenReturn(Optional.of(movimientoAnterior));

        BigDecimal saldoActual = movimientoService.obtenerSaldoActual(1L);

        assertEquals(new BigDecimal("2500.00"), saldoActual);
        verify(movimientoRepository, times(1)).findTopByCuentaIdOrderByFechaDesc(1L);
    }

    @Test
    void testObtenerSaldoActualSinMovimientos() {
        when(movimientoRepository.findTopByCuentaIdOrderByFechaDesc(1L))
                .thenReturn(Optional.empty());
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));

        BigDecimal saldoActual = movimientoService.obtenerSaldoActual(1L);

        assertEquals(new BigDecimal("2000.00"), saldoActual);
        verify(cuentaRepository, times(1)).findById(1L);
    }

    @Test
    void testRegistrarMovimientoTipoInvalido() {
        // Arrange
        when(cuentaRepository.findByNumeroCuenta("478758")).thenReturn(Optional.of(cuenta));
        when(movimientoRepository.findTopByCuentaIdOrderByFechaDesc(1L))
                .thenReturn(Optional.of(movimientoAnterior));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            movimientoService.registrarMovimiento(
                    "478758",
                    "TipoInvalido",
                    new BigDecimal("100.00")
            );
        });

        verify(movimientoRepository, never()).save(any(Movimiento.class));
    }
}