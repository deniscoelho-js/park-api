package br.com.park_api;

import br.com.park_api.dto.UsuarioLoginDto;
import br.com.park_api.exception.ErrorMessage;
import br.com.park_api.jwt.JwtToken;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/usuarios/usuario-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/usuarios/usuario-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AutenticationIT {

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void autenticar_ComCredenciaisValidas_RetornarTokenComStatus200(){
        JwtToken responseDto = webTestClient
                .post()
                .uri("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioLoginDto("ana@teste.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(JwtToken.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseDto).isNotNull();
    }

    @Test
    public void autenticar_ComCredenciaisInvalidas_RetornarErrorMessageComStatus400(){
        ErrorMessage responseDto = webTestClient
                .post()
                .uri("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioLoginDto("anamaria@teste.com", "123456"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseDto).isNotNull();
        Assertions.assertThat(responseDto.getStatus()).isEqualTo(400);

        responseDto = webTestClient
                .post()
                .uri("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioLoginDto("ana@teste.com", "000000"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseDto).isNotNull();
        Assertions.assertThat(responseDto.getStatus()).isEqualTo(400);
    }

    @Test
    public void autenticar_ComCredenciaisEmailInvalido_RetornarErrorMessageComStatus422(){
        ErrorMessage responseDto = webTestClient
                .post()
                .uri("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioLoginDto("", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseDto).isNotNull();
        Assertions.assertThat(responseDto.getStatus()).isEqualTo(422);

        responseDto = webTestClient
                .post()
                .uri("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioLoginDto("@test.com", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseDto).isNotNull();
        Assertions.assertThat(responseDto.getStatus()).isEqualTo(422);
    }

    @Test
    public void autenticar_ComPasswordInvalido_RetornarErrorMessageComStatus422(){
        ErrorMessage responseDto = webTestClient
                .post()
                .uri("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioLoginDto("ana@teste.com", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseDto).isNotNull();
        Assertions.assertThat(responseDto.getStatus()).isEqualTo(422);

        responseDto = webTestClient
                .post()
                .uri("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioLoginDto("ana@teste.com", "123"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseDto).isNotNull();
        Assertions.assertThat(responseDto.getStatus()).isEqualTo(422);

        responseDto = webTestClient
                .post()
                .uri("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioLoginDto("ana@teste.com", "1234567"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseDto).isNotNull();
        Assertions.assertThat(responseDto.getStatus()).isEqualTo(422);
    }
}
