package br.com.park_api.config;

import br.com.park_api.entity.Usuario;
import br.com.park_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class LoadDatabase {

    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository repository) {
        return args -> {
            repository.save(new Usuario(1L, "admin@gmail.com", passwordEncoder.encode("123456"), Usuario.Role.ROLE_ADMIN, LocalDateTime.now(), LocalDateTime.now(), "system", "system"));
            repository.save(new Usuario(2L, "lia@gmail.com", passwordEncoder.encode("123456"), Usuario.Role.ROLE_CLIENTE, LocalDateTime.now(), LocalDateTime.now(), "system", "system"));
            repository.save(new Usuario(3L, "ana@gmail.com", passwordEncoder.encode("123456"), Usuario.Role.ROLE_CLIENTE, LocalDateTime.now(), LocalDateTime.now(), "system", "system"));
        };
    }
}
