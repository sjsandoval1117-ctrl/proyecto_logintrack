package com.logintrack.logintrack_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LogintrackBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(LogintrackBackendApplication.class, args);
		System.out.println("==========================================================");
		System.out.println("   ¡SERVIDOR LOGINTRACK WMS INICIADO CON ÉXITO EN 8080!    ");
		System.out.println("==========================================================");
	}
}
