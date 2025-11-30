package org.example.cuentaservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.cuentaservice.dto.EstadoCuentaDto;
import org.example.cuentaservice.service.ReporteService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    @GetMapping
    public ResponseEntity<EstadoCuentaDto> generarEstadoCuenta(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam String clienteId) {

        EstadoCuentaDto estadoCuenta = reporteService.generarEstadoCuenta(fechaInicio, fechaFin, clienteId);
        return ResponseEntity.ok(estadoCuenta);
    }
}