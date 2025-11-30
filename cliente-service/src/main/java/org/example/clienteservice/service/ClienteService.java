package org.example.clienteservice.service;

import lombok.RequiredArgsConstructor;
import org.example.clienteservice.exception.ResourceNotFoundException;
import org.example.clienteservice.model.Cliente;
import org.example.clienteservice.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Cliente findById(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));
    }

    @Transactional(readOnly = true)
    public Cliente findByClienteId(String clienteId) {
        return clienteRepository.findByClienteId(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con clienteId: " + clienteId));
    }

    @Transactional
    public Cliente create(Cliente cliente) {
        if (cliente.getClienteId() == null || cliente.getClienteId().isEmpty()) {
            cliente.setClienteId(UUID.randomUUID().toString());
        }
        if (cliente.getEstado() == null) {
            cliente.setEstado(true);
        }
        return clienteRepository.save(cliente);
    }

    @Transactional
    public Cliente update(Long id, Cliente clienteDetails) {
        Cliente cliente = findById(id);
        cliente.setNombre(clienteDetails.getNombre());
        cliente.setGenero(clienteDetails.getGenero());
        cliente.setEdad(clienteDetails.getEdad());
        cliente.setIdentificacion(clienteDetails.getIdentificacion());
        cliente.setDireccion(clienteDetails.getDireccion());
        cliente.setTelefono(clienteDetails.getTelefono());
        cliente.setContrasena(clienteDetails.getContrasena());
        cliente.setEstado(clienteDetails.getEstado());
        return clienteRepository.save(cliente);
    }

    @Transactional
    public void delete(Long id) {
        Cliente cliente = findById(id);
        clienteRepository.delete(cliente);
    }
}