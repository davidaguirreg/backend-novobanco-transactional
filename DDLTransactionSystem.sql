CREATE DATABASE transaccional_novobanco
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE transaccional_novobanco;

CREATE TABLE cliente (
    id_cliente BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

CREATE TABLE tipo_cuenta (
    id_tipo_cuenta INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE cuenta (
    id_cuenta BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_cuenta VARCHAR(20) UNIQUE NOT NULL,
    id_tipo_cuenta INT NOT NULL,
    moneda VARCHAR(10) NOT NULL,
    saldo_disponible DECIMAL(15,2) NOT NULL DEFAULT 0,
    estado ENUM('activa', 'bloqueada', 'cerrada') NOT NULL,
    id_cliente BIGINT NOT NULL,
    CONSTRAINT chk_saldo_no_negativo CHECK (saldo_disponible >= 0),
    FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente),
    FOREIGN KEY (id_tipo_cuenta) REFERENCES tipo_cuenta(id_tipo_cuenta)
);

CREATE TABLE transaccion (
    id_transaccion BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo ENUM('deposito', 'retiro', 'transferencia') NOT NULL,
    fecha_hora_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    referencia_unica VARCHAR(100) UNIQUE NOT NULL,
    estado ENUM('exitosa', 'fallida', 'revertida') NOT NULL
);

CREATE TABLE movimiento (
    id_movimiento BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_transaccion BIGINT NOT NULL,
    id_cuenta BIGINT NOT NULL,
    tipo ENUM('debito', 'credito') NOT NULL,
    monto DECIMAL(15,2) NOT NULL,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado ENUM('activo', 'revertido') NOT NULL,
    FOREIGN KEY (id_transaccion) REFERENCES transaccion(id_transaccion),
    FOREIGN KEY (id_cuenta) REFERENCES cuenta(id_cuenta)
);

CREATE TABLE comision (
    id_comision BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo_transaccion ENUM('retiro', 'transferencia') NOT NULL,
    monto_fijo DECIMAL(10,2),
    porcentaje DECIMAL(5,2),
    moneda VARCHAR(10),
    activa BOOLEAN NOT NULL DEFAULT TRUE
);
/* INDICES NECESARIOS */
CREATE UNIQUE INDEX idx_numero_cuenta
ON cuenta (numero_cuenta);

CREATE INDEX idx_cuenta_cliente
ON cuenta (id_cliente);

CREATE INDEX idx_transaccion_fecha ON transaccion (fecha_hora_registro);

CREATE INDEX idx_movimiento_cuenta_fecha
ON movimiento (id_cuenta, fecha DESC);

CREATE INDEX idx_movimiento_transaccion
ON movimiento (id_transaccion);

CREATE INDEX idx_movimiento_fecha ON movimiento (fecha);




