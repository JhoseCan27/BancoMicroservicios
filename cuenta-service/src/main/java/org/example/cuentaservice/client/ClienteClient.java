package org.example.cuentaservice.client;

import lombok.RequiredArgsConstructor;
import org.example.cuentaservice.dto.ClienteDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class ClienteClient {

    private final RestTemplate restTemplate;

    @Value("${cliente.service.url}")
    private String clienteServiceUrl;

    public ClienteDto obtenerCliente(String clienteId) {
        String url = clienteServiceUrl + "/clientes/clienteId/" + clienteId;
        return restTemplate.getForObject(url, ClienteDto.class);
    }
}
