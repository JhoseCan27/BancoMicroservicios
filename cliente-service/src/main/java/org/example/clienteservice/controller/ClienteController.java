package org.example.clienteservice.controller;


import lombok.RequiredArgsConstructor;
import org.example.clienteservice.model.Cliente;
import org.example.clienteservice.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<Cliente>> getAllClientes() {
        return ResponseEntity.ok(clienteService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getClienteById(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.findById(id));
    }

    @GetMapping("/clienteId/{clienteId}")
    public ResponseEntity<Cliente> getClienteByClienteId(@PathVariable String clienteId) {
        return ResponseEntity.ok(clienteService.findByClienteId(clienteId));
    }

    @PostMapping
    public ResponseEntity<Cliente> createCliente(@RequestBody Cliente cliente) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clienteService.create(cliente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> updateCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        return ResponseEntity.ok(clienteService.update(id, cliente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
