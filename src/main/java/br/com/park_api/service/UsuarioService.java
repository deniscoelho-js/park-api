package br.com.park_api.service;

import br.com.park_api.entity.Usuario;
import br.com.park_api.exception.EntityNotFoundException;
import br.com.park_api.exception.UserEmailUniqueViolationException;
import br.com.park_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Usuario salvar(Usuario usuario){
        try {
            return usuarioRepository.save(usuario);
        } catch (DataIntegrityViolationException exception){
            throw new UserEmailUniqueViolationException(String.format("Usuário '%s' já cadastrado", usuario.getEmail()));
        }

    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id){
        return usuarioRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Usuário com id: '%s' não encontrado", id)));
    }

    @Transactional
    public Usuario alterarEmail(Long id, String novoEmail , String confirmaSenha){
        Usuario user = buscarPorId(id);

        if(!confirmaSenha.equals(user.getPassword())){
            throw new RuntimeException("Sua senha não confere.");
        }

        user.setEmail(novoEmail);
        return usuarioRepository.save(user);
    }

    @Transactional
    public Usuario alterarSenha(Long id, String senhaAtual, String novaSenha, String confirmaSenha){
        if(!novaSenha.equals(confirmaSenha)){
            throw new RuntimeException("Nova senha não confere com confirmação de senha");
        }

        Usuario user = buscarPorId(id);

        if(!user.getPassword().equals(senhaAtual)){
            throw new RuntimeException("Sua senha não confere.");
        }

        user.setPassword(novaSenha);
        return usuarioRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarTodos(){
        return usuarioRepository.findAll();
    }

    @Transactional
    public void deletarUsuario(Long id){
        Usuario user = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + id));
         usuarioRepository.deleteById(id);
    }

}
