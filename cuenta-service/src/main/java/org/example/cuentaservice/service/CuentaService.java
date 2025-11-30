package org.example.cuentaservice.service;


import lombok.RequiredArgsConstructor;
import org.example.cuentaservice.client.ClienteClient;
import org.example.cuentaservice.dto.ClienteDto;
import org.example.cuentaservice.exception.ResourceNotFoundException;
import org.example.cuentaservice.model.Cuenta;
import org.example.cuentaservice.repository.CuentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CuentaService {

    private final CuentaRepository cuentaRepository;
    private final ClienteClient clienteClient;

    @Transactional(readOnly = true)
    public List<Cuenta> findAll() {
        List<Cuenta> cuentas = cuentaRepository.findAll();
        cuentas.forEach(this::enrichWithClienteInfo);
        return cuentas;
    }

    @Transactional(readOnly = true)
    public Cuenta findById(Long id) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con id: " + id));
        enrichWithClienteInfo(cuenta);
        return cuenta;
    }

    @Transactional(readOnly = true)
    public Cuenta findByNumeroCuenta(String numeroCuenta) {
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada: " + numeroCuenta));
        enrichWithClienteInfo(cuenta);
        return cuenta;
    }

    @Transactional
    public Cuenta create(Cuenta cuenta) {
        // Verificar que el cliente existe
        clienteClient.obtenerCliente(cuenta.getClienteId());

        if (cuenta.getEstado() == null) {
            cuenta.setEstado(true);
        }
        return cuentaRepository.save(cuenta);
    }

    @Transactional
    public Cuenta update(Long id, Cuenta cuentaDetails) {
        Cuenta cuenta = findById(id);
        cuenta.setTipoCuenta(cuentaDetails.getTipoCuenta());
        cuenta.setSaldoInicial(cuentaDetails.getSaldoInicial());
        cuenta.setEstado(cuentaDetails.getEstado());
        return cuentaRepository.save(cuenta);
    }

    @Transactional
    public void delete(Long id) {
        Cuenta cuenta = findById(id);
        cuentaRepository.delete(cuenta);
    }

    private void enrichWithClienteInfo(Cuenta cuenta) {
        try {
            ClienteDto cliente = clienteClient.obtenerCliente(cuenta.getClienteId());
            cuenta.setNombreCliente(cliente.getNombre());
        } catch (Exception e) {
            cuenta.setNombreCliente("Cliente no encontrado");
        }
    }
}
