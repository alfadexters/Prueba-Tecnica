package com.devops.prueba_tecnica.Controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DevOpsControllerTest {

    private static final String VALID_API_KEY = "2f5ae96c-b558-4c7b-a590-a501ae1c36f6";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void givenValidApiKey_whenPostDevOps_thenReturnExpectedMessage() throws Exception {
        String jsonRequest = """
                {
                  "message": "This is a test",
                  "to": "Juan Perez",
                  "from": "Rita Asturia",
                  "timeToLifeSec": 45
                }
                """;

        mockMvc.perform(post("/DevOps")
                        .header("X-Parse-REST-API-Key", VALID_API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Hello Juan Perez your message will be send"));
    }

    @Test
    void givenInvalidApiKey_whenPostDevOps_thenReturnErrorAndUnauthorized() throws Exception {
        String jsonRequest = """
                {
                  "message": "This is a test",
                  "to": "Juan Perez",
                  "from": "Rita Asturia",
                  "timeToLifeSec": 45
                }
                """;

        mockMvc.perform(post("/DevOps")
                        .header("X-Parse-REST-API-Key", "INVALIDA")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("ERROR"));
    }

    @Test
    void whenGetDevOps_thenReturnErrorString() throws Exception {
        mockMvc.perform(get("/DevOps"))
                .andExpect(status().isOk())
                .andExpect(content().string("ERROR"));
    }

    @Test
    void whenPutDevOps_thenReturnErrorString() throws Exception {
        mockMvc.perform(put("/DevOps"))
                .andExpect(status().isOk())
                .andExpect(content().string("ERROR"));
    }
}
