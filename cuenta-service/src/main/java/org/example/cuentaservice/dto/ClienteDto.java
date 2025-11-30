package org.example.cuentaservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDto {
    private String clienteId;
    private String nombre;
    private String identificacion;
}
