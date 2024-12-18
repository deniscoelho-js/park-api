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
                .headers(JwtAuthentication.getAuthorization(webTestClient, "ana@teste.com", "123456"))
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
                .headers(JwtAuthentication.getAuthorization(webTestClient, "ana@teste.com", "123456"))
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
                .headers(JwtAuthentication.getAuthorization(webTestClient, "ana@teste.com", "123456"))
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
                .headers(JwtAuthentication.getAuthorization(webTestClient, "ana@teste.com", "123456"))
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
                .headers(JwtAuthentication.getAuthorization(webTestClient, "ana@teste.com", "123456"))
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
                .headers(JwtAuthentication.getAuthorization(webTestClient, "ana@teste.com", "123456"))
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
                .headers(JwtAuthentication.getAuthorization(webTestClient, "ana@teste.com", "123456"))
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
                .headers(JwtAuthentication.getAuthorization(webTestClient, "ana@teste.com", "123456"))
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
                .headers(JwtAuthentication.getAuthorization(webTestClient, "ana@teste.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseDto).isNotNull();
        Assertions.assertThat(responseDto.getStatus()).isEqualTo(404);
    }

    @Test
    public void buscarUsuario_ComUsuarioClienteBuscandoOutroCliente_RetornarErrorMessageComStatus403(){
        ErrorMessage responseDto = webTestClient
                .get()
                .uri("/api/v1/usuarios/102")
                .headers(JwtAuthentication.getAuthorization(webTestClient, "bia@teste.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseDto).isNotNull();
        Assertions.assertThat(responseDto.getStatus()).isEqualTo(403);
    }

    @Test
    public void buscarUsuario_ComIdExistente_RetornarUsuarioCriadoComStatus200(){
        UsuarioResponseDto responseDto = webTestClient
                .get()
                .uri("/api/v1/usuarios/100")
                .headers(JwtAuthentication.getAuthorization(webTestClient, "ana@teste.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UsuarioResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseDto).isNotNull();
        Assertions.assertThat(responseDto.getId()).isEqualTo(100);
        Assertions.assertThat(responseDto.getEmail()).isEqualTo("ana@teste.com");
        Assertions.assertThat(responseDto.getRole()).isEqualTo("ADMIN");

        responseDto = webTestClient
                .get()
                .uri("/api/v1/usuarios/101")
                .headers(JwtAuthentication.getAuthorization(webTestClient, "ana@teste.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UsuarioResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseDto).isNotNull();
        Assertions.assertThat(responseDto.getId()).isEqualTo(101);
        Assertions.assertThat(responseDto.getEmail()).isEqualTo("bia@teste.com");
        Assertions.assertThat(responseDto.getRole()).isEqualTo("CLIENTE");

        responseDto = webTestClient
                .get()
                .uri("/api/v1/usuarios/101")
                .headers(JwtAuthentication.getAuthorization(webTestClient, "bia@teste.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UsuarioResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseDto).isNotNull();
        Assertions.assertThat(responseDto.getId()).isEqualTo(101);
        Assertions.assertThat(responseDto.getEmail()).isEqualTo("bia@teste.com");
        Assertions.assertThat(responseDto.getRole()).isEqualTo("CLIENTE");
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
                .headers(JwtAuthentication.getAuthorization(webTestClient, "ana@teste.com", "123456"))
                .bodyValue(new AlterarSenhaRequestDto("123456", "763757", "763757"))
                .exchange()
                .expectStatus().isNoContent();

        webTestClient
                .put()
                .uri("/api/v1/usuarios/alterarsenha/101")
                .headers(JwtAuthentication.getAuthorization(webTestClient, "bia@teste.com", "123456"))
                .bodyValue(new AlterarSenhaRequestDto("123456", "763757", "763757"))
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void alterarSenha_ComUsuariosDiferentes_DeveRetornarStatus403() {

        ErrorMessage responseDto = webTestClient
                .put()
                .uri("/api/v1/usuarios/alterarsenha/0")
                .headers(JwtAuthentication.getAuthorization(webTestClient, "ana@teste.com", "123456"))
                .bodyValue(new AlterarSenhaRequestDto("123456", "763757", "763757"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseDto).isNotNull();
        Assertions.assertThat(responseDto.getStatus()).isEqualTo(403);

        responseDto = webTestClient
                .put()
                .uri("/api/v1/usuarios/alterarsenha/0")
                .headers(JwtAuthentication.getAuthorization(webTestClient, "bia@teste.com", "123456"))
                .bodyValue(new AlterarSenhaRequestDto("123456", "763757", "763757"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseDto).isNotNull();
        Assertions.assertThat(responseDto.getStatus()).isEqualTo(403);

    }


    @Test
    public void alterarSenha_ComCamposInvalidos_DeveRetornarStatus422() {

        ErrorMessage responseDto = webTestClient
                .put()
                .uri("/api/v1/usuarios/alterarsenha/100")
                .headers(JwtAuthentication.getAuthorization(webTestClient, "ana@teste.com", "123456"))
                .bodyValue(new AlterarSenhaRequestDto("", "", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseDto).isNotNull();
        Assertions.assertThat(responseDto.getStatus()).isEqualTo(422);

         webTestClient
                .put()
                .uri("/api/v1/usuarios/alterarsenha/100")
                 .headers(JwtAuthentication.getAuthorization(webTestClient, "ana@teste.com", "123456"))
                .bodyValue(new AlterarSenhaRequestDto("1234", "1234", "1234"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseDto).isNotNull();
        Assertions.assertThat(responseDto.getStatus()).isEqualTo(422);
//
         webTestClient
                .put()
                .uri("/api/v1/usuarios/alterarsenha/100")
                 .headers(JwtAuthentication.getAuthorization(webTestClient, "ana@teste.com", "123456"))
                .bodyValue(new AlterarSenhaRequestDto("1234567", "1234567", "1234567"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseDto).isNotNull();
        Assertions.assertThat(responseDto.getStatus()).isEqualTo(422);
    }

    @Test
    public void alterarSenha_ComCamposInvalidos_DeveRetornarStatus400() {

        ErrorMessage responseDto = webTestClient
                .put()
                .uri("/api/v1/usuarios/alterarsenha/100")
                .headers(JwtAuthentication.getAuthorization(webTestClient, "ana@teste.com", "123456"))
                .bodyValue(new AlterarSenhaRequestDto("123456", "123456", "000000"))
                .exchange()
                .expectStatus().isEqualTo(400)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseDto).isNotNull();
        Assertions.assertThat(responseDto.getStatus()).isEqualTo(400);

        webTestClient
                .put()
                .uri("/api/v1/usuarios/alterarsenha/100")
                .headers(JwtAuthentication.getAuthorization(webTestClient, "ana@teste.com", "123456"))
                .bodyValue(new AlterarSenhaRequestDto("000000", "123456", "123456"))
                .exchange()
                .expectStatus().isEqualTo(400)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseDto).isNotNull();
        Assertions.assertThat(responseDto.getStatus()).isEqualTo(400);

    }
}
