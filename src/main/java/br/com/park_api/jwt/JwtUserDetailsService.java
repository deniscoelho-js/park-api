package br.com.park_api.jwt;

import br.com.park_api.dto.mapper.UsuarioMapper;
import br.com.park_api.entity.Usuario;
import br.com.park_api.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
    private final UsuarioService usuarioService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioService.buscarPorEmail(email);
        return new JwtUserDetails(usuario);
    }

    public JwtToken getTokenAuthenticated(String email){
        Usuario.Role role = usuarioService.buscarRolePorEmail(email);
        return JwtUtils.createToken(email, role.name().substring("Role_".length()));
    }
}
