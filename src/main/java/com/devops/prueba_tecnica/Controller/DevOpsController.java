package com.devops.prueba_tecnica.Controller;

import com.devops.prueba_tecnica.DTO.DevOpsRequest;
import com.devops.prueba_tecnica.DTO.DevOpsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/DevOps")
public class DevOpsController {

    private static final String API_KEY_HEADER = "X-Parse-REST-API-Key";
    private static final String EXPECTED_API_KEY = "2f5ae96c-b558-4c7b-a590-a501ae1c36f6";

    /**
     * Endpoint principal: solo permite POST
     */
    @PostMapping
    public ResponseEntity<?> handleDevOps(
            @RequestHeader(value = API_KEY_HEADER, required = false) String apiKey,
            @RequestBody DevOpsRequest request
    ) {
        // Validar API Key
        if (apiKey == null || !EXPECTED_API_KEY.equals(apiKey)) {
            // Podríamos devolver 401, pero el cuerpo sigue siendo "ERROR"
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("ERROR");
        }

        String responseMessage = "Hello " + request.getTo() + " your message will be send";
        DevOpsResponse response = new DevOpsResponse(responseMessage);

        return ResponseEntity.ok(response);
    }

    /**
     * Otros métodos HTTP deben devolver la cadena "ERROR"
     */
    @RequestMapping(method = {
            RequestMethod.GET,
            RequestMethod.PUT,
            RequestMethod.DELETE,
            RequestMethod.PATCH,
            RequestMethod.HEAD,
            RequestMethod.OPTIONS,
            RequestMethod.TRACE
    })
    public ResponseEntity<String> handleOtherMethods() {
        return ResponseEntity.ok("ERROR");
    }
}
