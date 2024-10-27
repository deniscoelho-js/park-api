package br.com.park_api.config;

import br.com.park_api.entity.Usuario;
import br.com.park_api.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository repository) {
        return args -> {
            repository.save(new Usuario(1L, "ana@gmail.com", "123456", Usuario.Role.ROLE_ADMIN, LocalDateTime.now(), LocalDateTime.now(), "system", "system"));
            repository.save(new Usuario(2L, "bia@gmail.com", "123456", Usuario.Role.ROLE_ADMIN, LocalDateTime.now(), LocalDateTime.now(), "system", "system"));
            repository.save(new Usuario(3L, "jov@gmail.com", "123456", Usuario.Role.ROLE_ADMIN, LocalDateTime.now(), LocalDateTime.now(), "system", "system"));
        };
    }
}
