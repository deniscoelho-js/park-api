package br.com.park_api;

import br.com.park_api.dto.UsuarioLoginDto;
import br.com.park_api.jwt.JwtToken;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;


import java.util.function.Consumer;

public class JwtAuthentication {
    public static Consumer<HttpHeaders> getAuthorization(WebTestClient client, String email, String password){
        String token = client
                .post()
                .uri("/api/v1/auth")
                .bodyValue(new UsuarioLoginDto(email, password))
                .exchange()
                .expectStatus().isOk()
                .expectBody(JwtToken.class)
                .returnResult().getResponseBody().getToken();
        return headers -> headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    }
}
