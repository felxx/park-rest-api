package com.felxx.park_rest_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.felxx.park_rest_api.web.dto.PageableDto;
import com.felxx.park_rest_api.web.dto.ParkingLotCreateDto;

@Sql(scripts = "/sql/parking-lots/parking-lots-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/parking-lots/parking-lots-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ParkingLotIT {
    
    @Autowired
    WebTestClient webTestClient;

    @Test
    public void createCheckIn_WithValidData_ReturnCreatedandLocation() {
        ParkingLotCreateDto parkingLotCreateDto = ParkingLotCreateDto.builder()
                .licensePlate("WER-1111").make("Toyota").model("Corolla")
                .color("Red").clientCpf("09191773016").build();
            
        webTestClient.post().uri("/api/v1/parking-lots/check-in")
        .contentType(MediaType.APPLICATION_JSON)
        .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@email.com.br", "123456"))
        .bodyValue(parkingLotCreateDto)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().exists(HttpHeaders.LOCATION)
        .expectBody()
        .jsonPath("licensePlate").isEqualTo("WER-1111")
        .jsonPath("make").isEqualTo("Toyota")
        .jsonPath("model").isEqualTo("Corolla")
        .jsonPath("color").isEqualTo("Red")
        .jsonPath("clientCpf").isEqualTo("09191773016")
        .jsonPath("receipt").exists()
        .jsonPath("parkingSpaceCode").exists();
    }

    @Test
    public void createCheckIn_WithClientRole_ReturnErrorWithStatus403() {
        ParkingLotCreateDto parkingLotCreateDto = ParkingLotCreateDto.builder()
                .licensePlate("WER-1111").make("Toyota").model("Corolla")
                .color("Red").clientCpf("09191773016").build();
            
        webTestClient.post().uri("/api/v1/parking-lots/check-in")
        .contentType(MediaType.APPLICATION_JSON)
        .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "bia@email.com.br", "123456"))
        .bodyValue(parkingLotCreateDto)
        .exchange()
        .expectStatus().isForbidden()
        .expectBody()
        .jsonPath("status").isEqualTo("403")
        .jsonPath("path").isEqualTo("/api/v1/parking-lots/check-in")
        .jsonPath("method").isEqualTo("POST");
    }


    @Test
    public void createCheckIn_WithInvalidData_ReturnErrorWithStatus422() {
        ParkingLotCreateDto parkingLotCreateDto = ParkingLotCreateDto.builder()
                .licensePlate("").make("").model("")
                .color("").clientCpf("").build();
            
        webTestClient.post().uri("/api/v1/parking-lots/check-in")
        .contentType(MediaType.APPLICATION_JSON)
        .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "bia@email.com.br", "123456"))
        .bodyValue(parkingLotCreateDto)
        .exchange()
        .expectStatus().isEqualTo(422)
        .expectBody()
        .jsonPath("status").isEqualTo("422")
        .jsonPath("path").isEqualTo("/api/v1/parking-lots/check-in")
        .jsonPath("method").isEqualTo("POST");
    }

    @Test
    public void createCheckIn_WithInexistingCpf_ReturnErrorWithStatus404() {
        ParkingLotCreateDto parkingLotCreateDto = ParkingLotCreateDto.builder()
                .licensePlate("WER-1111").make("Toyota").model("Corolla")
                .color("Red").clientCpf("53442084059").build();
            
        webTestClient.post().uri("/api/v1/parking-lots/check-in")
        .contentType(MediaType.APPLICATION_JSON)
        .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@email.com.br", "123456"))
        .bodyValue(parkingLotCreateDto)
        .exchange()
        .expectStatus().isNotFound()
        .expectBody()
        .jsonPath("status").isEqualTo("404")
        .jsonPath("path").isEqualTo("/api/v1/parking-lots/check-in")
        .jsonPath("method").isEqualTo("POST");
    }

    @Test
    public void findCheckIn_WithAdminRole_ReturnParkingSpaceWithStatus200() {
        webTestClient.get().uri("/api/v1/parking-lots/check-in/20230313-101300")
        .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@email.com.br", "123456"))
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("licensePlate").isEqualTo("FIT-1020")
        .jsonPath("make").isEqualTo("FIAT")
        .jsonPath("model").isEqualTo("PALIO")
        .jsonPath("color").isEqualTo("VERDE")
        .jsonPath("clientCpf").isEqualTo("98401203015")
        .jsonPath("receipt").isEqualTo("20230313-101300")
        .jsonPath("entryDate").isEqualTo("2023-03-13 10:15:00")
        .jsonPath("parkingSpaceCode").isEqualTo("A-01");
    }

    @Test
    public void findCheckIn_WithClientRole_ReturnParkingSpaceWithStatus200() {
        webTestClient.get().uri("/api/v1/parking-lots/check-in/20230313-101300")
        .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "bia@email.com.br", "123456"))
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("licensePlate").isEqualTo("FIT-1020")
        .jsonPath("make").isEqualTo("FIAT")
        .jsonPath("model").isEqualTo("PALIO")
        .jsonPath("color").isEqualTo("VERDE")
        .jsonPath("clientCpf").isEqualTo("98401203015")
        .jsonPath("receipt").isEqualTo("20230313-101300")
        .jsonPath("entryDate").isEqualTo("2023-03-13 10:15:00")
        .jsonPath("parkingSpaceCode").isEqualTo("A-01");
    }

    @Test
    public void findCheckIn_WithInexistentReceipt_ReturnErrorWithStatus404() {
        webTestClient.get().uri("/api/v1/parking-lots/check-in/{receipt}", "20230313-999999")
        .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@email.com.br", "123456"))
        .exchange()
        .expectStatus().isNotFound()
        .expectBody()
        .jsonPath("status").isEqualTo("404")
        .jsonPath("path").isEqualTo("/api/v1/parking-lots/check-in/20230313-999999")
        .jsonPath("method").isEqualTo("GET");
    }

    @Test
    public void checkOut_WithExistingReceipt_ReturnStatus200() {
        webTestClient.put().uri("/api/v1/parking-lots/check-out/{receipt}", "20230313-101300")
        .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@email.com.br", "123456"))
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("licensePlate").isEqualTo("FIT-1020")
        .jsonPath("make").isEqualTo("FIAT")
        .jsonPath("model").isEqualTo("PALIO")
        .jsonPath("color").isEqualTo("VERDE")
        .jsonPath("clientCpf").isEqualTo("98401203015")
        .jsonPath("receipt").isEqualTo("20230313-101300")
        .jsonPath("entryDate").isEqualTo("2023-03-13 10:15:00")
        .jsonPath("exitDate").exists()
        .jsonPath("parkingSpaceCode").isEqualTo("A-01")
        .jsonPath("price").exists()
        .jsonPath("discount").exists();
    }

    @Test
    public void checkOut_WithInexistentReceipt_ReturnErrorWithStatus404() {
        webTestClient.put().uri("/api/v1/parking-lots/check-out/{receipt}", "20230313-999999")
        .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@email.com.br", "123456"))
        .exchange()
        .expectStatus().isNotFound()
        .expectBody()
        .jsonPath("status").isEqualTo("404")
        .jsonPath("path").isEqualTo("/api/v1/parking-lots/check-out/20230313-999999")
        .jsonPath("method").isEqualTo("PUT");
    }

    @Test
    public void checkOut_WithClientRole_ReturnErrorWithStatus403() {
        webTestClient.put().uri("/api/v1/parking-lots/check-out/{receipt}", "20230313-101300")
        .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "bia@email.com.br", "123456"))
        .exchange()
        .expectStatus().isForbidden()
        .expectBody()
        .jsonPath("status").isEqualTo("403")
        .jsonPath("path").isEqualTo("/api/v1/parking-lots/check-out/20230313-101300")
        .jsonPath("method").isEqualTo("PUT");
    }

    @Test
    public void findParkings_WithClientCpf_ReturnSuccess() {
        PageableDto responseBody = webTestClient.get().uri("/api/v1/parking-lots/cpf/{cpf}?size=1&page=0", "98401203015")
        .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@email.com.br", "123456"))
        .exchange()
        .expectStatus().isOk()
        .expectBody(PageableDto.class)
        .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getContent()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(2);
        org.assertj.core.api.Assertions.assertThat(responseBody.getNumber()).isEqualTo(0);
        org.assertj.core.api.Assertions.assertThat(responseBody.getSize()).isEqualTo(1);

        responseBody = webTestClient.get().uri("/api/v1/parking-lots/cpf/{cpf}?size=1&page=1", "98401203015")
        .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "ana@email.com.br", "123456"))
        .exchange()
        .expectStatus().isOk()
        .expectBody(PageableDto.class)
        .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getContent()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(2);
        org.assertj.core.api.Assertions.assertThat(responseBody.getNumber()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(responseBody.getSize()).isEqualTo(1);
    }

    @Test
    public void findParkings_WithClientRole_ReturnErrorWithStatus403() {
        webTestClient.get().uri("/api/v1/parking-lots/cpf/{cpf}", "98401203015")
        .headers(JwtAuthentication.getHeaderAuthorization(webTestClient, "bia@email.com.br", "123456"))
        .exchange()
        .expectStatus().isForbidden()
        .expectBody()
        .jsonPath("status").isEqualTo("403")
        .jsonPath("path").isEqualTo("/api/v1/parking-lots/cpf/98401203015")
        .jsonPath("method").isEqualTo("PUT");
    }
}
