package org.example.cuentaservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadoCuentaDto {
    private String clienteNombre;
    private String clienteId;
    private List<CuentaMovimientosDto> cuentas;
}

