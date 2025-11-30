package org.example.clienteservice.service;

import org.example.clienteservice.exception.ResourceNotFoundException;
import org.example.clienteservice.model.Cliente;
import org.example.clienteservice.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Jose Lema");
        cliente.setGenero("Masculino");
        cliente.setEdad(35);
        cliente.setIdentificacion("098254785");
        cliente.setDireccion("Otavalo sn y principal");
        cliente.setTelefono("1234");
        cliente.setClienteId("jose-lema-001");
        cliente.setContrasena("1234");
        cliente.setEstado(true);
    }

    @Test
    void testFindByIdExitoso() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        Cliente found = clienteService.findById(1L);

        assertNotNull(found);
        assertEquals("Jose Lema", found.getNombre());
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdFallido() {
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            clienteService.findById(999L);
        });
    }

    @Test
    void testCrearExitoso() {
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        Cliente created = clienteService.create(cliente);

        assertNotNull(created);
        assertEquals("Jose Lema", created.getNombre());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void testEliinarExitoso() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        doNothing().when(clienteRepository).delete(cliente);

        clienteService.delete(1L);

        verify(clienteRepository, times(1)).delete(cliente);
    }
}