package br.com.park_api.controller;


import br.com.park_api.dto.AlterarEmailRequestDto;
import br.com.park_api.dto.UsuarioRequestDto;
import br.com.park_api.dto.UsuarioResponseDto;
import br.com.park_api.dto.AlterarSenhaRequestDto;
import br.com.park_api.dto.mapper.UsuarioMapper;
import br.com.park_api.entity.Usuario;
import br.com.park_api.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    @PostMapping
    public ResponseEntity<UsuarioResponseDto> create(@Valid @RequestBody UsuarioRequestDto usuarioRequestDto){
        Usuario user = usuarioService.salvar(UsuarioMapper.toUsuario(usuarioRequestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toUsuarioResponseDto(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> buscarPoId(@PathVariable Long id){
        Usuario user = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(UsuarioMapper.toUsuarioResponseDto(user));
    }

    @PutMapping("/alteraremail/{id}")
    public ResponseEntity<UsuarioResponseDto> alterar(@PathVariable Long id, @Valid  @RequestBody AlterarEmailRequestDto dto){
        Usuario user = usuarioService.alterarEmail(id, dto.getNovoEmail(), dto.getConfirmaSenha());
        return ResponseEntity.ok(UsuarioMapper.toUsuarioResponseDto(user));
    }

    @PutMapping("/alterarsenha/{id}")
    public ResponseEntity<UsuarioResponseDto> alterarSenha(@PathVariable Long id, @Valid  @RequestBody AlterarSenhaRequestDto dto){
        Usuario user = usuarioService.alterarSenha(id, dto.getSenhaAtual(), dto.getNovaSenha(), dto.getConfirmaSenha());
        return ResponseEntity.ok(UsuarioMapper.toUsuarioResponseDto(user));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> getAll(){
        List<Usuario> users = usuarioService.buscarTodos();
        return ResponseEntity.ok(UsuarioMapper.toListResponseUsuarioDto(users));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable Long id){
         usuarioService.deletarUsuario(id);
         return ResponseEntity.ok("Usuário deletado com sucesso!");
    }
}
