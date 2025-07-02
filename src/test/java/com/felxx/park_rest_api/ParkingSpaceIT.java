package com.felxx.park_rest_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.felxx.park_rest_api.web.dto.ParkingSpaceCreateDto;

@Sql(scripts = "/sql/parking-spaces/parking-spaces-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/parking-spaces/parking-spaces-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ParkingSpaceIT {
    
    @Autowired
    WebTestClient webTestClient;

    @Test
    public void createParkingSpace_WithValidData_ReturnLocationStatus201() {
        webTestClient.post()
            .uri("api/v1/parking-spaces")
            .contentType(MediaType.APPLICATION_JSON)
            .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@gmail.com", "123456"))
            .bodyValue(new ParkingSpaceCreateDto("A-05", "AVAILABLE"))
            .exchange()
            .expectStatus().isCreated()
            .expectHeader().exists(HttpHeaders.LOCATION);
    }

    @Test
    public void createParkingSpace_WithExistingCode_ReturnErrorMessageWithStatus409() {
        webTestClient.post()
            .uri("api/v1/parking-spaces")
            .contentType(MediaType.APPLICATION_JSON)
            .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@gmail.com", "123456"))
            .bodyValue(new ParkingSpaceCreateDto("A-01", "AVAILABLE"))
            .exchange()
            .expectStatus().isEqualTo(409)
            .expectBody()
            .jsonPath("status").isEqualTo(409)
            .jsonPath("method").isEqualTo("POST")
            .jsonPath("path").isEqualTo("/api/v1/parking-spaces");
    }

    @Test
    public void createParkingSpace_WithInvalidData_ReturnErrorMessageWithStatus422() {
        webTestClient.post()
            .uri("api/v1/parking-spaces")
            .contentType(MediaType.APPLICATION_JSON)
            .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@gmail.com", "123456"))
            .bodyValue(new ParkingSpaceCreateDto("", ""))
            .exchange()
            .expectStatus().isEqualTo(422)
            .expectBody()
            .jsonPath("status").isEqualTo(422)
            .jsonPath("method").isEqualTo("POST")
            .jsonPath("path").isEqualTo("/api/v1/parking-spaces");
    
        webTestClient.post()
                .uri("api/v1/parking-spaces")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@gmail.com", "123456"))
                .bodyValue(new ParkingSpaceCreateDto("aaaaaa", "DISABLED"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody()
                .jsonPath("status").isEqualTo(422)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/parking-spaces");
    }

    @Test
    public void findParkingSpace_WithExistingCode_ReturnParkingSpaceWithStatus200() {
        webTestClient.get()
            .uri("api/v1/parking-spaces/A-01")
            .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@gmail.com", "123456"))
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("id").isEqualTo(10)
            .jsonPath("code").isEqualTo("A-01")
            .jsonPath("status").isEqualTo("AVAILABLE");
    }

    @Test
    public void findParkingSpace_WithInexistingCode_ReturnErrorMessageWithStatus404() {
        webTestClient.get()
            .uri("api/v1/parking-spaces/{code}", "A-99")
            .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@gmail.com", "123456"))
            .exchange()
            .expectStatus().isEqualTo(404)
            .expectBody()
            .jsonPath("status").isEqualTo(404)
            .jsonPath("method").isEqualTo("GET")
            .jsonPath("path").isEqualTo("/api/v1/parking-spaces/A-99");
    }
}
