package com.felxx.park_rest_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.felxx.park_rest_api.entities.Client;
import com.felxx.park_rest_api.web.dto.ClientCreateDto;
import com.felxx.park_rest_api.web.dto.ClientResponseDto;
import com.felxx.park_rest_api.web.dto.PageableDto;
import com.felxx.park_rest_api.web.exceptions.ErrorMessage;

@Sql(scripts = "/sql/clients/clients-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/clients/clients-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClientIT {
    
    @Autowired
    WebTestClient webTestClient;

    @Test
    public void createClient_ValidData_ReturnCreatedClientWithStatus201() {
        ClientResponseDto responseBody = webTestClient.post()
            .uri("/api/v1/clients")
            .contentType(MediaType.APPLICATION_JSON)
            .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "toby@gmail.com", "123456"))
            .bodyValue(new ClientCreateDto("Tobias Ferreira", "80456316000"))
            .exchange()
            .expectStatus().isCreated()
            .expectBody(ClientResponseDto.class)
            .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getName()).isEqualTo("Tobias Ferreira");
        org.assertj.core.api.Assertions.assertThat(responseBody.getCpf()).isEqualTo("80456316000");
    }

    @Test
    public void createClient_ValidCpfAlreadyRegistered_ReturnErrorMessageWithStatus409() {
        ErrorMessage responseBody = webTestClient.post()
            .uri("/api/v1/clients")
            .contentType(MediaType.APPLICATION_JSON)
            .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "toby@gmail.com", "123456"))
            .bodyValue(new ClientCreateDto("Tobias Ferreira", "82856169082"))
            .exchange()
            .expectStatus().isEqualTo(409)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);
    }

    @Test
    public void createClient_WithInvalidData_ReturnErrorMessageWithStatus422() {
        ErrorMessage responseBody = webTestClient.post()
            .uri("/api/v1/clients")
            .contentType(MediaType.APPLICATION_JSON)
            .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "toby@gmail.com", "123456"))
            .bodyValue(new ClientCreateDto("", ""))
            .exchange()
            .expectStatus().isEqualTo(422)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        webTestClient.post()
            .uri("/api/v1/clients")
            .contentType(MediaType.APPLICATION_JSON)
            .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "toby@gmail.com", "123456"))
            .bodyValue(new ClientCreateDto("BB", "00000000000"))
            .exchange()
            .expectStatus().isEqualTo(422)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        webTestClient.post()
            .uri("/api/v1/clients")
            .contentType(MediaType.APPLICATION_JSON)
            .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "toby@gmail.com", "123456"))
            .bodyValue(new ClientCreateDto("BB", "804.563.160-00"))
            .exchange()
            .expectStatus().isEqualTo(422)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void createClient_UserNotAllowed_ReturnErrorMessageWithStatus403() {
        ErrorMessage responseBody = webTestClient.post()
            .uri("/api/v1/clients")
            .contentType(MediaType.APPLICATION_JSON)
            .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@gmail.com", "123456"))
            .bodyValue(new ClientCreateDto("Tobias Ferreira", "80456316000"))
            .exchange()
            .expectStatus().isEqualTo(403)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    @Test
    public void findClient_ExistingIdByAdmin_ReturnClientWithStatus200() {
        ClientResponseDto responseBody = webTestClient.get()
            .uri("/api/v1/clients/10")
            .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@gmail.com", "123456"))
            .exchange()
            .expectStatus().isOk()
            .expectBody(ClientResponseDto.class)
            .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(10);
    }

    @Test
    public void findClient_InexistingIdByAdmin_ReturnErrorMessageWithStatus404() {
        ErrorMessage responseBody = webTestClient.get()
            .uri("/api/v1/clients/0")
            .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@gmail.com", "123456"))
            .exchange()
            .expectStatus().isNotFound()
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }

    @Test
    public void findClient_ExistingIdByClient_ReturnErrorMessageWithStatus403() {
        ErrorMessage responseBody = webTestClient.get()
            .uri("/api/v1/clients/10")
            .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "bia@gmail.com", "123456"))
            .exchange()
            .expectStatus().isForbidden()
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    @Test
    public void findClient_WithPaginationByAdmin_ReturnClientsWithStatus200() {
        PageableDto responseBody = webTestClient.get()
            .uri("/api/v1/clients")
            .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@gmail.com", "123456"))
            .exchange()
            .expectStatus().isOk()
            .expectBody(PageableDto.class)
            .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getContent().size()).isEqualTo(2);
        org.assertj.core.api.Assertions.assertThat(responseBody.getNumber()).isEqualTo(0);
        org.assertj.core.api.Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(1);

        responseBody = webTestClient.get()
            .uri("/api/v1/clients?page=1&size=1")
            .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@gmail.com", "123456"))
            .exchange()
            .expectStatus().isOk()
            .expectBody(PageableDto.class)
            .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getContent().size()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(responseBody.getNumber()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(2);
    }

    @Test
    public void findClient_WithPaginationByClient_ReturnErrorMessageWithStatus403() {
        ErrorMessage responseBody = webTestClient.get()
            .uri("/api/v1/clients")
            .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "bia@gmail.com", "123456"))
            .exchange()
            .expectStatus().isForbidden()
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    @Test
    public void findClient_WithDataFromUserToken_ReturnClientWithStatus200() {
        ClientResponseDto responseBody = webTestClient.get()
            .uri("/api/v1/clients/details")
            .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "bia@gmail.com", "123456"))
            .exchange()
            .expectStatus().isOk()
            .expectBody(ClientResponseDto.class)
            .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getCpf()).isEqualTo("82856169082");
        org.assertj.core.api.Assertions.assertThat(responseBody.getName()).isEqualTo("Roberto Gomes");
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(20);
    }

    @Test
    public void findClient_WithDataFromAdminToken_ReturnErrorMessageWithStatus403() {
        ErrorMessage responseBody = webTestClient.get()
            .uri("/api/v1/clients/details")
            .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@gmail.com", "123456"))
            .exchange()
            .expectStatus().isForbidden()
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }
}
