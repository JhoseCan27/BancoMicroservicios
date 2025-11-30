package org.example.cuentaservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoRequest {
    private String numeroCuenta;
    private String tipoMovimiento;
    private BigDecimal valor;
}
