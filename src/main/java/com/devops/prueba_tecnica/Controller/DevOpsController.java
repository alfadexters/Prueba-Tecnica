package com.devops.prueba_tecnica.Controller;

import com.devops.prueba_tecnica.DTO.DevOpsRequest;
import com.devops.prueba_tecnica.DTO.DevOpsResponse;
import com.devops.prueba_tecnica.Service.ApiAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/DevOps")
public class DevOpsController {

    private final ApiAuthService apiAuthService;

    public DevOpsController(ApiAuthService apiAuthService) {
        this.apiAuthService = apiAuthService;
    }

    // ÚNICO método válido: POST
    @PostMapping
    public ResponseEntity<?> handleDevOps(
            @RequestHeader(value = ApiAuthService.API_KEY_HEADER, required = false) String apiKey,
            @RequestHeader(value = ApiAuthService.JWT_HEADER, required = false) String jwt,
            @RequestBody DevOpsRequest request
    ) {

        if (!apiAuthService.isAuthorized(apiKey, jwt)) {
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
