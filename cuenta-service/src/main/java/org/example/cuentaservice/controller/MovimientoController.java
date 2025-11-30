package org.example.cuentaservice.controller;


import lombok.RequiredArgsConstructor;
import org.example.cuentaservice.dto.MovimientoRequest;
import org.example.cuentaservice.model.Movimiento;
import org.example.cuentaservice.service.MovimientoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoService movimientoService;

    @PostMapping
    public ResponseEntity<Movimiento> registrarMovimiento(@RequestBody MovimientoRequest request) {
        Movimiento movimiento = movimientoService.registrarMovimiento(
                request.getNumeroCuenta(),
                request.getTipoMovimiento(),
                request.getValor()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(movimiento);
    }
}