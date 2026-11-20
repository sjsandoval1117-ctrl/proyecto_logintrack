# LogiTrack — Sistema Integral de Gestión Logística y Auditoría de Inventario

![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=flat-square&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-JWT_%2B_BCrypt-6DB33F?style=flat-square&logo=springsecurity&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=flat-square&logo=mysql&logoColor=white)
![Frontend](https://img.shields.io/badge/Frontend-HTML5_%7C_CSS3_%7C_JS-F7DF1E?style=flat-square&logo=javascript&logoColor=black)

Plataforma web full-stack diseñada para el control operativo de bodegas, gestión de movimientos de inventario y trazabilidad de auditoría en tiempo real. El repositorio sigue una arquitectura desacoplada que separa la capa de presentación (`frontend-vs`) de la API RESTful y persistencia (`backend-intellij`).

---
## LINK:https://decisions-config-lottery-contains.trycloudflare.com

## Arquitectura y Estructura del Repositorio

```text
proyecto_logintrack/
├── frontend-vs/                 # Cliente Web (SPA / Vistas Modulares)
│   ├── Index.html               # Punto de entrada y vista de autenticación
│   ├── css/                     # Hojas de estilo y diseño responsivo
│   ├── js/                      # Consumo de API REST, manejo de tokens y lógica UI
│   └── view/                    # Módulos y vistas internas del sistema
├── backend-intellij/            # Servicio Backend (API RESTful en Spring Boot)
│   ├── src/
│   │   └── main/
│   │       ├── java/            # Controladores, Servicios, Repositorios, Entidades, DTOs y Seguridad
│   │       └── resources/       # Configuración de aplicación (application.properties / yml)
│   └── pom.xml                  # Gestión de dependencias y construcción con Maven
├── .gitignore
└── README.md
