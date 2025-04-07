package com.gamecenter;

import com.gamecenter.dto.SnakeMoveRequest;
import com.gamecenter.model.SnakeState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class IntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void fullGameFlowTest() {
        String gameId = restTemplate.postForObject(
            "http://localhost:" + port + "/api/snake/new", 
            null, 
            String.class
        );
        assertNotNull(gameId);
        assertFalse(gameId.isEmpty());

        SnakeMoveRequest moveRequest = new SnakeMoveRequest(gameId, "RIGHT");
        ResponseEntity<SnakeState> response = restTemplate.postForEntity(
            "http://localhost:" + port + "/api/snake/move",
            moveRequest,
            SnakeState.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("RIGHT", response.getBody().getDirection().name());
    }
}