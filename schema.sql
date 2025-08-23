CREATE DATABASE IF NOT EXISTS fidebank
  CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE fidebank;

-- Usuario (opcional, si no tienes uno)
CREATE USER IF NOT EXISTS 'fidebank_user'@'localhost' IDENTIFIED BY 'MaXFiB9';
GRANT ALL PRIVILEGES ON fidebank.* TO 'fidebank_user'@'localhost';
FLUSH PRIVILEGES;

-- CLIENTE
CREATE TABLE IF NOT EXISTS cliente (
  id           BIGINT PRIMARY KEY AUTO_INCREMENT,
  nombre       VARCHAR(80)  NOT NULL,
  apellido     VARCHAR(80)  NOT NULL,
  email        VARCHAR(120) NOT NULL UNIQUE,
  telefono     VARCHAR(30),
  direccion    VARCHAR(200),
  creado_en    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- CUENTA
CREATE TABLE IF NOT EXISTS cuenta (
  id             BIGINT PRIMARY KEY AUTO_INCREMENT,
  numero         VARCHAR(32) NOT NULL UNIQUE,
  cliente_id     BIGINT NOT NULL,
  pin_hash       VARBINARY(128) NOT NULL,
  pin_salt       VARBINARY(32)  NOT NULL,
  saldo          DECIMAL(15,2) NOT NULL DEFAULT 0.00,
  estado         ENUM('ACTIVA','BLOQUEADA') NOT NULL DEFAULT 'ACTIVA',
  intentos_fallidos INT NOT NULL DEFAULT 0,
  bloqueada_hasta   TIMESTAMP NULL DEFAULT NULL,
  creado_en      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_cuenta_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);

CREATE INDEX idx_cuenta_numero ON cuenta(numero);

-- TRANSACCION
CREATE TABLE IF NOT EXISTS transaccion (
  id                 BIGINT PRIMARY KEY AUTO_INCREMENT,
  cuenta_id          BIGINT NOT NULL,
  tipo               ENUM('DEPOSITO','RETIRO','TRANSFERENCIA') NOT NULL,
  monto              DECIMAL(15,2) NOT NULL,
  fecha              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  estado             ENUM('EXITOSA','FALLIDA') NOT NULL,
  cuenta_destino_id  BIGINT NULL,
  detalle            VARCHAR(500),
  CONSTRAINT fk_tx_cuenta FOREIGN KEY (cuenta_id) REFERENCES cuenta(id),
  CONSTRAINT fk_tx_cuenta_dest FOREIGN KEY (cuenta_destino_id) REFERENCES cuenta(id)
);

CREATE INDEX idx_tx_cuenta_fecha ON transaccion(cuenta_id, fecha);