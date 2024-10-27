package br.com.park_api;

import br.com.park_api.dto.AlterarSenhaRequestDto;
import br.com.park_api.dto.UsuarioRequestDto;
import br.com.park_api.dto.UsuarioResponseDto;
import br.com.park_api.exception.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.assertj.core.api.Assertions;
import org.springframework.web.reactive.function.client.ExchangeStrategies;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/usuarios/usuario-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/usuarios/usuario-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UsuarioIT {


    @Autowired
    WebTestClient webTestClient;

    @Test
    public void createUsuario_ComEmailEPasswordValidos_RetornarUsuarioCriadoComStatus201(){
        UsuarioResponseDto responseDto = webTestClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioRequestDto("jhon@teste.com", "123456"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UsuarioResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseDto).isNotNull();
        Assertions.assertThat(responseDto.getId()).isNotNull();
        Assertions.assertThat(responseDto.getEmail()).isEqualTo("jhon@teste.com");
        Assertions.assertThat(responseDto.getRole()).isEqualTo("CLIENTE");

    }

    @Test
    public void createUsuario_ComEmailInvalidos_RetornarErrorMessage422(){
        ErrorMessage response = webTestClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioRequestDto("", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(422);


        response = webTestClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioRequestDto("jhon@", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(422);

        response = webTestClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioRequestDto("jhon@teste", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(422);


        response = webTestClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioRequestDto("jhon@teste.", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(422);

    }

    @Test
    public void createUsuario_ComPasswordilInvalidos_RetornarErrorMessage422(){
        ErrorMessage response = webTestClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioRequestDto("jhon@teste.com", "12345"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(422);


        response = webTestClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioRequestDto("jhon@teste.com", "1234567"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(422);

        response = webTestClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioRequestDto("jhon@teste.com", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(422);
    }

    @Test
    public void createUsuario_ComEmailRepetido_RetornarErrorMessageComStatus409(){
        ErrorMessage responseDto = webTestClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioRequestDto("ana@teste.com", "123456"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseDto).isNotNull();
        Assertions.assertThat(responseDto.getStatus()).isEqualTo(409);

    }



    @Test
    public void buscarUsuario_ComIdInexistente_RetornarErrorMessageComStatus404(){
        ErrorMessage responseDto = webTestClient
                .get()
                .uri("/api/v1/usuarios/0")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseDto).isNotNull();
        Assertions.assertThat(responseDto.getStatus()).isEqualTo(404);


    }

    @Test
    public void buscarUsuario_ComIdExistente_RetornarUsuarioCriadoComStatus200(){
        UsuarioResponseDto responseDto = webTestClient
                .get()
                .uri("/api/v1/usuarios/100")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UsuarioResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseDto).isNotNull();
        Assertions.assertThat(responseDto.getId()).isEqualTo(100);
        Assertions.assertThat(responseDto.getEmail()).isEqualTo("ana@teste.com");
//        System.out.println(responseDto.getEmail());
        Assertions.assertThat(responseDto.getRole()).isEqualTo("ADMIN");

    }

    @Test
    public void alterarSenha_ComDadosValidos_DeveRetornarStatus204() {
//        Long userId = 100L;
//        AlterarSenhaRequestDto alterarSenhaRequestDto = new AlterarSenhaRequestDto("123456", "763757", "763757");

//        webTestClient
//                .put()
//                .uri("/api/v1/usuarios/alterarsenha/{id}", userId)
//                .bodyValue(alterarSenhaRequestDto)
//                .exchange()
//                .expectStatus().isNoContent();

        webTestClient
                .put()
                .uri("/api/v1/usuarios/alterarsenha/100")
                .bodyValue(new AlterarSenhaRequestDto("123456", "763757", "763757"))
                .exchange()
                .expectStatus().isNoContent();
    }

}
