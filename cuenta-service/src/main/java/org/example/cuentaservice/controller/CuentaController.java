package org.example.cuentaservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.cuentaservice.model.Cuenta;
import org.example.cuentaservice.service.CuentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/cuentas")
@RequiredArgsConstructor
public class CuentaController {

    private final CuentaService cuentaService;

    @GetMapping
    public ResponseEntity<List<Cuenta>> getAllCuentas() {
        return ResponseEntity.ok(cuentaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cuenta> getCuentaById(@PathVariable Long id) {
        return ResponseEntity.ok(cuentaService.findById(id));
    }

    @GetMapping("/numero/{numeroCuenta}")
    public ResponseEntity<Cuenta> getCuentaByNumero(@PathVariable String numeroCuenta) {
        return ResponseEntity.ok(cuentaService.findByNumeroCuenta(numeroCuenta));
    }

    @PostMapping
    public ResponseEntity<Cuenta> createCuenta(@RequestBody Cuenta cuenta) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cuentaService.create(cuenta));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cuenta> updateCuenta(@PathVariable Long id, @RequestBody Cuenta cuenta) {
        return ResponseEntity.ok(cuentaService.update(id, cuenta));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCuenta(@PathVariable Long id) {
        cuentaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}