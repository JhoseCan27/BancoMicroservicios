package org.example.cuentaservice.service;

import lombok.RequiredArgsConstructor;
import org.example.cuentaservice.client.ClienteClient;
import org.example.cuentaservice.dto.ClienteDto;
import org.example.cuentaservice.dto.CuentaMovimientosDto;
import org.example.cuentaservice.dto.EstadoCuentaDto;
import org.example.cuentaservice.dto.MovimientoDto;
import org.example.cuentaservice.model.Cuenta;
import org.example.cuentaservice.model.Movimiento;
import org.example.cuentaservice.repository.CuentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private final CuentaRepository cuentaRepository;
    private final MovimientoService movimientoService;
    private final ClienteClient clienteClient;

    @Transactional(readOnly = true)
    public EstadoCuentaDto generarEstadoCuenta(LocalDate fechaInicio, LocalDate fechaFin, String clienteId) {
        ClienteDto cliente = clienteClient.obtenerCliente(clienteId);

        List<Cuenta> cuentas = cuentaRepository.findByClienteId(clienteId);

        LocalDateTime fechaInicioTime = fechaInicio.atStartOfDay();
        LocalDateTime fechaFinTime = fechaFin.atTime(LocalTime.MAX);

        List<CuentaMovimientosDto> cuentasDTO = cuentas.stream()
                .map(cuenta -> {
                    List<Movimiento> movimientos = movimientoService.obtenerMovimientos(
                            cuenta.getNumeroCuenta(), fechaInicioTime, fechaFinTime);

                    List<MovimientoDto> movimientosDTO = movimientos.stream()
                            .map(m -> new MovimientoDto(
                                    m.getFecha(),
                                    m.getTipoMovimiento(),
                                    m.getValor(),
                                    m.getSaldo()
                            ))
                            .collect(Collectors.toList());

                    CuentaMovimientosDto cuentaDTO = new CuentaMovimientosDto();
                    cuentaDTO.setNumeroCuenta(cuenta.getNumeroCuenta());
                    cuentaDTO.setTipoCuenta(cuenta.getTipoCuenta());
                    cuentaDTO.setSaldoInicial(cuenta.getSaldoInicial());
                    cuentaDTO.setEstado(cuenta.getEstado());
                    cuentaDTO.setSaldoActual(movimientoService.obtenerSaldoActual(cuenta.getId()));
                    cuentaDTO.setMovimientos(movimientosDTO);

                    return cuentaDTO;
                })
                .collect(Collectors.toList());

        EstadoCuentaDto estadoCuenta = new EstadoCuentaDto();
        estadoCuenta.setClienteNombre(cliente.getNombre());
        estadoCuenta.setClienteId(cliente.getClienteId());
        estadoCuenta.setCuentas(cuentasDTO);

        return estadoCuenta;
    }
}
