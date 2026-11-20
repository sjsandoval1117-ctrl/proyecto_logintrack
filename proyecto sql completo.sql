CREATE DATABASE IF NOT EXISTS logitrack_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE logitrack_db;

-- 1. Tabla de usuarios (coincide con Config.java: username, password, role, status)
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'ADMIN',
    status VARCHAR(50) NOT NULL DEFAULT 'APROBADO'
);

-- 2. Tabla de bodegas (coincide con renderTablaBodegas y initBodegaCRUD)
CREATE TABLE IF NOT EXISTS bodegas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL,
    ubicacion VARCHAR(200) NOT NULL,
    capacidad INT NOT NULL,
    encargado VARCHAR(120) NOT NULL
);

-- 3. Tabla de productos (coincide con renderTablaProductos y initProductoCRUD)
CREATE TABLE IF NOT EXISTS productos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL,
    categoria VARCHAR(100) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    precio DECIMAL(12, 2) NOT NULL DEFAULT 0.00
);

-- 4. Tabla de movimientos (coincide con renderTablaMovimientos y initMovimientoCRUD)
CREATE TABLE IF NOT EXISTS movimientos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo_movimiento VARCHAR(50) NOT NULL,
    bodega_destino VARCHAR(120) NOT NULL,
    bodega_origen VARCHAR(120) NULL,
    cantidad INT NOT NULL,
    producto_id BIGINT NOT NULL,
    usuario_responsable VARCHAR(100) NOT NULL,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_movimiento_producto
        FOREIGN KEY (producto_id) REFERENCES productos(id)
        ON DELETE RESTRICT ON UPDATE CASCADE
);

-- 5. Tabla de auditorías (coincide con renderTablaAuditorias)
CREATE TABLE IF NOT EXISTS auditorias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo_operacion VARCHAR(50) NOT NULL,
    entidad_afectada VARCHAR(100) NOT NULL,
    usuario_responsable VARCHAR(100) NOT NULL,
    fecha_hora DATETIME DEFAULT CURRENT_TIMESTAMP,
    detalle_antes TEXT NULL,
    detalle_despues TEXT NULL
);

-- Datos iniciales de ejemplo (opcionales para pruebas)
INSERT INTO bodegas (nombre, ubicacion, capacidad, encargado) VALUES
('Bodega Central', 'Zona Industrial - Bucaramanga', 1000, 'Sara17'),
('Bodega Norte', 'Floridablanca', 500, 'Encargado Norte');

INSERT INTO productos (nombre, categoria, stock, precio) VALUES
('Caja Estándar', 'Empaques', 150, 4500.00),
('Pallet Madera', 'Logística', 40, 38000.00);