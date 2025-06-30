package com.felxx.park_rest_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.felxx.park_rest_api.jwt.JwtToken;
import com.felxx.park_rest_api.web.dto.UserLoginDto;
import com.felxx.park_rest_api.web.exceptions.ErrorMessage;

@Sql(scripts = "/sql/users/users-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/users/users-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthIT {

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void Auth_WithValidCredentials_ReturnStatus200() {
        JwtToken responseBody = webTestClient.post()
            .uri("/api/v1/auth")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UserLoginDto("ana@gmail.com", "123456"))
            .exchange()
            .expectStatus().isOk()
            .expectBody(JwtToken.class)
            .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    }

    @Test
    public void Auth_WithInvalidCredentials_ReturnErrorMessageStatus400() {
        ErrorMessage responseBody = webTestClient.post()
            .uri("/api/v1/auth")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UserLoginDto("invalid@gmail.com", "123456"))
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);

        webTestClient.post()
            .uri("/api/v1/auth")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UserLoginDto("ana@gmail.com", "000000"))
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);
    }

    @Test
    public void Auth_WithInvalidUsername_ReturnErrorMessageStatus422() {
        ErrorMessage responseBody = webTestClient.post()
            .uri("/api/v1/auth")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UserLoginDto("", "123456"))
            .exchange()
            .expectStatus().isEqualTo(422)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        webTestClient.post()
            .uri("/api/v1/auth")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UserLoginDto("@email.com", "123456"))
            .exchange()
            .expectStatus().isEqualTo(422)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void Auth_WithInvalidPassword_ReturnErrorMessageStatus422() {
        ErrorMessage responseBody = webTestClient.post()
            .uri("/api/v1/auth")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UserLoginDto("ana@gmail.com", "1234567"))
            .exchange()
            .expectStatus().isEqualTo(422)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        webTestClient.post()
            .uri("/api/v1/auth")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UserLoginDto("ana@email.com", ""))
            .exchange()
            .expectStatus().isEqualTo(422)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    
        webTestClient.post()
            .uri("/api/v1/auth")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UserLoginDto("ana@email.com", "123"))
            .exchange()
            .expectStatus().isEqualTo(422)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }
}
