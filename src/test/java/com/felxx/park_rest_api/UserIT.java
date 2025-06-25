package com.felxx.park_rest_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.felxx.park_rest_api.web.dto.UserCreateDto;
import com.felxx.park_rest_api.web.dto.UserPasswordDto;
import com.felxx.park_rest_api.web.dto.UserResponseDto;
import com.felxx.park_rest_api.web.exceptions.ErrorMessage;

@Sql(scripts = "/sql/users/users-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/users/users-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIT {
    
    @Autowired
    WebTestClient webTestClient;

    @Test
    public void createUser_ValidUsernameAndPassword_UserCreatedStatus201(){
        UserResponseDto responseBody = webTestClient.post()
            .uri("/api/v1/users")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UserCreateDto("tody@gmail.com", "123456"))
            .exchange()
            .expectStatus().isCreated()
            .expectBody(UserResponseDto.class)
            .returnResult().getResponseBody();
        
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("tody@gmail.com");
        org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENT");
    }

    @Test
    public void createUser_InvalidUsername_ReturnErrorMessageStatus422(){
        ErrorMessage responseBody = webTestClient.post()
            .uri("/api/v1/users")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UserCreateDto("", "123456"))
            .exchange()
            .expectStatus().isEqualTo(422)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();
        
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = webTestClient.post()
            .uri("/api/v1/users")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UserCreateDto("tody@", "123456"))
            .exchange()
            .expectStatus().isEqualTo(422)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();
        
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = webTestClient.post()
            .uri("/api/v1/users")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UserCreateDto("tody@gmail", "123456"))
            .exchange()
            .expectStatus().isEqualTo(422)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();
        
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void createUser_InvalidPassword_ReturnErrorMessageStatus422(){
        ErrorMessage responseBody = webTestClient.post()
            .uri("/api/v1/users")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UserCreateDto("igor@gmail.com", ""))
            .exchange()
            .expectStatus().isEqualTo(422)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();
        
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = webTestClient.post()
            .uri("/api/v1/users")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UserCreateDto("igor@gmail.com", "1234"))
            .exchange()
            .expectStatus().isEqualTo(422)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();
        
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = webTestClient.post()
            .uri("/api/v1/users")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UserCreateDto("igor@gmail.com", "123456789"))
            .exchange()
            .expectStatus().isEqualTo(422)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();
        
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void createUser_UsernameAlreadyExists_ReturnErrorMessageStatus409(){
        ErrorMessage responseBody = webTestClient.post()
            .uri("/api/v1/users")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UserCreateDto("ana@gmail.com", "123456"))
            .exchange()
            .expectStatus().isEqualTo(409)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();
        
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);
    }

    @Test
    public void findUser_ExistingId_ReturnUserWithStatus200(){
        UserResponseDto responseBody = webTestClient.get()
            .uri("/api/v1/users/101")
            .exchange()
            .expectStatus().isOk()
            .expectBody(UserResponseDto.class)
            .returnResult().getResponseBody();
        
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(101);
        org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("ana@gmail.com");
        org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("ADMIN");
    }

    @Test
    public void findUser_InexistingId_ReturnErrorMessageWithStatus404(){
        ErrorMessage responseBody = webTestClient.get()
            .uri("/api/v1/users/0")
            .exchange()
            .expectStatus().isNotFound()
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();
        
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }

    @Test
    public void changePassword_WithValidInput_ReturnStatus204(){
        webTestClient.patch()
            .uri("/api/v1/users/101")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UserPasswordDto("123456", "654321", "654321"))
            .exchange()
            .expectStatus().isNoContent();
    }

    @Test
    public void changePassword_InexistingId_ReturnErrorMessageWithStatus404(){
        ErrorMessage responseBody = webTestClient.patch()
            .uri("/api/v1/users/0")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UserPasswordDto("123456", "654321", "654321"))
            .exchange()
            .expectStatus().isNotFound()
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();
        
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }

    @Test
    public void changePassword_WithoutValidInput_ReturnErrorMessageWithStatus422(){
        ErrorMessage responseBody = webTestClient.patch()
            .uri("/api/v1/users/101")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UserPasswordDto("12345", "12345", "12345"))
            .exchange()
            .expectStatus().isEqualTo(422)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();
        
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = webTestClient.patch()
            .uri("/api/v1/users/101")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UserPasswordDto("1234567", "1234567", "1234567"))
            .exchange()
            .expectStatus().isEqualTo(422)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();
        
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = webTestClient.patch()
            .uri("/api/v1/users/101")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UserPasswordDto("", "", ""))
            .exchange()
            .expectStatus().isEqualTo(422)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();
        
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void changePassword_PasswordsMismatch_ReturnErrorMessageWithStatus400(){
        ErrorMessage responseBody = webTestClient.patch()
            .uri("/api/v1/users/101")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UserPasswordDto("123456", "654321", "000000"))
            .exchange()
            .expectStatus().isEqualTo(400)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();
        
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);
    }

    @Test
    public void changePassword_InvalidCurrentPassword_ReturnErrorMessageWithStatus400(){
        ErrorMessage responseBody = webTestClient.patch()
            .uri("/api/v1/users/101")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UserPasswordDto("000000", "654321", "654321"))
            .exchange()
            .expectStatus().isEqualTo(400)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();
        
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);
    }
}
