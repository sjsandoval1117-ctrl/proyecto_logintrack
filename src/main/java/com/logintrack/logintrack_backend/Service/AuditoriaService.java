package com.logintrack.logintrack_backend.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logintrack.logintrack_backend.Model.Auditoria;
import com.logintrack.logintrack_backend.Repository.AuditoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditoriaService {

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public void registrarAuditoria(String operacion, String entidad, String usuario, Object antes, Object despues) {
        try {
            Auditoria aud = new Auditoria();
            aud.setTipoOperacion(operacion);
            aud.setEntidadAfectada(entidad);
            aud.setUsuarioResponsable(usuario);
            aud.setFechaHora(LocalDateTime.now());

            if (antes != null) {
                aud.setJsonAntes(objectMapper.writeValueAsString(antes));
            }
            if (despues != null) {
                aud.setJsonDespues(objectMapper.writeValueAsString(despues));
            }

            auditoriaRepository.save(aud);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
