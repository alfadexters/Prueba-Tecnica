package com.devops.prueba_tecnica.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ApiAuthService {

    public static final String API_KEY_HEADER = "X-Parse-REST-API-Key";
    public static final String JWT_HEADER = "X-JWT-KWT";

    @Value("${security.api-key-value}")
    private String expectedApiKey;

    @Value("${security.jwt-required:true}")
    private boolean jwtRequired;

    /**
     * Validar la combinación de API Key y JWT.
     *   JWT, por ahora, solo se valida que no esté vacío cuando es requerido.
     *   (en un entorno real aquí se validaría la firma, expiración, issuer, etc.)
     */
    public boolean isAuthorized(String apiKey, String jwtToken) {
        if (apiKey == null || !apiKey.equals(expectedApiKey)) {
            return false;
        }

        if (!jwtRequired) {
            return true;
        }

        // Validación simple de JWT para esta prueba técnica:
        // requerido que exista y no esté en blanco.
        return jwtToken != null && !jwtToken.isBlank();
    }
}

