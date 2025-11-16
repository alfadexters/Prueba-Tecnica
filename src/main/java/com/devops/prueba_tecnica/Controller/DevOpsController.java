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

    // SOLO POST es el método válido
    @PostMapping
    public ResponseEntity<?> handleDevOps(
            @RequestHeader(value = API_KEY_HEADER, required = false) String apiKey,
            @RequestBody DevOpsRequest request
    ) {
        if (apiKey == null || !EXPECTED_API_KEY.equals(apiKey)) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("ERROR");
        }

        String responseMessage = "Hello " + request.getTo() + " your message will be send";
        DevOpsResponse response = new DevOpsResponse(responseMessage);

        return ResponseEntity.ok(response);
    }

    // TODOS los otros métodos HTTP deben devolver "ERROR" (incluyendo GET)
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
