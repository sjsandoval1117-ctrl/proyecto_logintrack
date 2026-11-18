# LogiTrack WMS Backend & Frontend

API REST en **Spring Boot 3.2.5** con Java 21, MySQL (normalizado en 3FN), seguridad con Spring Security + JWT, solicitudes de registro con aprobación de administrador, auditoría completa con JPA (registrando JSON de estado `antes` y `después`), manejo global de excepciones con `@ControllerAdvice`, validaciones y frontend básico en HTML/CSS/JS.

## Instrucciones de Instalación y Ejecución:
1. Crear base de datos en MySQL: `logintrack_db`.
2. Ejecutar el script `schema.sql` ubicado en `src/main/resources/`.
3. Abrir el proyecto en IntelliJ IDEA como proyecto Maven.
4. Ejecutar la clase `LogintrackBackendApplication.java` (el puerto por defecto es `8080`).
5. Se creará automáticamente un administrador por defecto (`Sara17` / `password123`).
6. Abrir los archivos en la carpeta `frontend/` en el navegador para probar el login y el dashboard de stock bajo.
