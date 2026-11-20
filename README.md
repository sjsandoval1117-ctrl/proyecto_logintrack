# LogiTrack — Sistema Integral de Gestión Logística y Auditoría de Inventario

![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=flat-square&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-JWT_%2B_BCrypt-6DB33F?style=flat-square&logo=springsecurity&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=flat-square&logo=mysql&logoColor=white)
![Frontend](https://img.shields.io/badge/Frontend-HTML5_%7C_CSS3_%7C_JS-F7DF1E?style=flat-square&logo=javascript&logoColor=black)

Plataforma web full-stack diseñada para el control operativo de bodegas, gestión de movimientos de inventario y trazabilidad de auditoría en tiempo real. El repositorio sigue una arquitectura desacoplada que separa la capa de presentación (`frontend-vs`) de la API RESTful en Spring Boot (`src/main/java/com/logintrack/logintrack_backend`) y el script de base de datos (`proyecto sql completo.sql`).

---

## Aclaración: URL Dinámica del Túnel (`cloudflared`)

Durante las sesiones de pruebas, demostraciones o revisiones externas, el acceso al backend local (`http://localhost:8080`) se expone mediante **Cloudflare Tunnel (`cloudflared`)** en modalidad rápida. Por esta razón, se debe tener en cuenta lo siguiente:

- **Carácter dinámico de la URL base:** Cada vez que se inicia o reinicia el túnel, Cloudflare asigna una nueva dirección pública temporal (`https://<subdominio-aleatorio>.trycloudflare.com`), por lo que el enlace no permanece constante entre ejecuciones.
- **Vigencia limitada al ciclo del túnel:** Si se cierra la consola donde se ejecuta el túnel o se genera una nueva sesión, la URL anterior deja de estar disponible y pierde validez para futuras consultas.
- **Actualización obligatoria en el cliente:** Tras cada reinicio del túnel, es necesario copiar la nueva URL generada y actualizar la configuración base (`API_BASE`) en el frontend (sin barra inclinada `/` al final) antes de realizar pruebas de autenticación o consumo de endpoints.

### Comando de ejecución del túnel

Con el servidor backend en ejecución en el puerto `8080`, inicie el túnel ejecutando el siguiente comando en terminal:

```bash
cloudflared tunnel --url http://localhost:8080
