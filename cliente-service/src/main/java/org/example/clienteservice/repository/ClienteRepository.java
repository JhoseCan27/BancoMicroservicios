package org.example.clienteservice.repository;

import org.example.clienteservice.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByClienteId(String clienteId);
    Optional<Cliente> findByIdentificacion(String identificacion);
}

