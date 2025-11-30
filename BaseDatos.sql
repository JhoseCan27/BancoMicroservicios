-- Script para Cliente Service Database
-- Base de datos: cliente_db

CREATE TABLE IF NOT EXISTS persona (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    genero VARCHAR(10) NOT NULL,
    edad INTEGER NOT NULL,
    identificacion VARCHAR(20) NOT NULL UNIQUE,
    direccion VARCHAR(200) NOT NULL,
    telefono VARCHAR(15) NOT NULL
);

CREATE TABLE IF NOT EXISTS cliente (
    id BIGINT PRIMARY KEY REFERENCES persona(id),
    cliente_id VARCHAR(255) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT TRUE
);

-- Datos de prueba para Cliente Service
INSERT INTO persona (nombre, genero, edad, identificacion, direccion, telefono) VALUES
('Jose Lema', 'Masculino', 35, '098254785', 'Otavalo sn y principal', '1234'),
('Marianela Montalvo', 'Femenino', 28, '097548965', 'Amazonas y NNUU', '5678'),
('Juan Osorio', 'Masculino', 42, '098874587', '13 junio y Equinoccial', '1245')
ON CONFLICT (identificacion) DO NOTHING;

INSERT INTO cliente (id, cliente_id, contrasena, estado) VALUES
(1, 'jose-lema-001', '1234', TRUE),
(2, 'marianela-montalvo-002', '5678', TRUE),
(3, 'juan-osorio-003', '1245', TRUE)
ON CONFLICT (cliente_id) DO NOTHING;

-- =====================================================
-- Script para Cuenta Service Database
-- Base de datos: cuenta_db

CREATE TABLE IF NOT EXISTS cuenta (
    id BIGSERIAL PRIMARY KEY,
    numero_cuenta VARCHAR(50) NOT NULL UNIQUE,
    tipo_cuenta VARCHAR(20) NOT NULL,
    saldo_inicial DECIMAL(15,2) NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT TRUE,
    cliente_id VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS movimiento (
    id BIGSERIAL PRIMARY KEY,
    fecha TIMESTAMP NOT NULL,
    tipo_movimiento VARCHAR(20) NOT NULL,
    valor DECIMAL(15,2) NOT NULL,
    saldo DECIMAL(15,2) NOT NULL,
    cuenta_id BIGINT NOT NULL REFERENCES cuenta(id)
);

-- Datos de prueba para Cuenta Service
INSERT INTO cuenta (numero_cuenta, tipo_cuenta, saldo_inicial, estado, cliente_id) VALUES
('478758', 'Ahorros', 2000.00, TRUE, 'jose-lema-001'),
('225487', 'Corriente', 100.00, TRUE, 'marianela-montalvo-002'),
('495878', 'Ahorros', 0.00, TRUE, 'juan-osorio-003'),
('496825', 'Ahorros', 540.00, TRUE, 'marianela-montalvo-002'),
('585545', 'Corriente', 1000.00, TRUE, 'jose-lema-001')
ON CONFLICT (numero_cuenta) DO NOTHING;