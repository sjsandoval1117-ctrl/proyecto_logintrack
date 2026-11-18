-- Script único de base de datos normalizado en Tercera Forma Normal (3FN)
CREATE DATABASE IF NOT EXISTS logintrack_db;
USE logintrack_db;

CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL, -- 'ADMIN' o 'EMPLEADO'
    status VARCHAR(20) NOT NULL -- 'PENDIENTE', 'APROBADO', 'RECHAZADO'
);

CREATE TABLE IF NOT EXISTS bodegas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    ubicacion VARCHAR(150) NOT NULL,
    capacidad INT NOT NULL,
    encargado VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS productos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    stock INT NOT NULL,
    precio DOUBLE NOT NULL
);

CREATE TABLE IF NOT EXISTS movimientos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha DATETIME NOT NULL,
    tipo_movimiento VARCHAR(20) NOT NULL, -- 'ENTRADA', 'SALIDA', 'TRANSFERENCIA'
    usuario_responsable VARCHAR(50) NOT NULL,
    bodega_origen VARCHAR(100),
    bodega_destino VARCHAR(100),
    cantidad INT NOT NULL,
    producto_id BIGINT,
    FOREIGN KEY (producto_id) REFERENCES productos(id)
);

CREATE TABLE IF NOT EXISTS auditorias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo_operacion VARCHAR(20) NOT NULL, -- 'INSERT', 'UPDATE', 'DELETE'
    entidad_afectada VARCHAR(50) NOT NULL,
    usuario_responsable VARCHAR(50) NOT NULL,
    fecha_hora DATETIME NOT NULL,
    json_antes TEXT,
    json_despues TEXT
);
