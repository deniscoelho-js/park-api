package br.com.park_api.controller;


import br.com.park_api.dto.AlterarEmailRequestDto;
import br.com.park_api.dto.UsuarioRequestDto;
import br.com.park_api.dto.UsuarioResponseDto;
import br.com.park_api.dto.AlterarSenhaRequestDto;
import br.com.park_api.dto.mapper.UsuarioMapper;
import br.com.park_api.entity.Usuario;
import br.com.park_api.exception.ErrorMessage;
import br.com.park_api.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Usuários", description = "Contém todas as operações para leitura, cadastro e edição dos usuários")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    @Operation(summary = "Criar novo usuário", description = "Recurso para criar novo usuário",
        responses = {
                 @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
                @ApiResponse(responseCode = "409", description = "Usuário e-mail já cadastrado no sistema",
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                @ApiResponse(responseCode = "422", description = "Dados de entrada inválidos",
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
        }
    )
    @PostMapping
    public ResponseEntity<UsuarioResponseDto> create(@Valid @RequestBody UsuarioRequestDto usuarioRequestDto){
        Usuario user = usuarioService.salvar(UsuarioMapper.toUsuario(usuarioRequestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toUsuarioResponseDto(user));
    }

    @Operation(summary = "Recuperar usuário pelo id", description = "Recuperar usuário pelo id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso recuperado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Recurso não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> buscarPoId(@PathVariable Long id){
        Usuario user = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(UsuarioMapper.toUsuarioResponseDto(user));
    }

    @Operation(summary = "Atualizar e-mail", description = "Atualizar e-mail",
            responses = {
                    @ApiResponse(responseCode = "204", description = "E-mail atualizado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Senha não confere",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Recurso não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @PutMapping("/alteraremail/{id}")
    public ResponseEntity<UsuarioResponseDto> alterar(@PathVariable Long id, @Valid  @RequestBody AlterarEmailRequestDto dto){
        Usuario user = usuarioService.alterarEmail(id, dto.getNovoEmail(), dto.getConfirmaSenha());
        return ResponseEntity.ok(UsuarioMapper.toUsuarioResponseDto(user));
    }

    @Operation(summary = "Atualizar senha", description = "Atualizar senha",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Senha atualizado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Senha não confere",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Recurso não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @PutMapping("/alterarsenha/{id}")
    public ResponseEntity<Void> alterarSenha(@PathVariable Long id, @Valid  @RequestBody AlterarSenhaRequestDto dto){
        Usuario user = usuarioService.alterarSenha(id, dto.getSenhaAtual(), dto.getNovaSenha(), dto.getConfirmaSenha());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Recuperar todos os usuários", description = "Recuperar todos os usuários",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recursos recuperados com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Recursos não encontrados",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> getAll(){
        List<Usuario> users = usuarioService.buscarTodos();
        return ResponseEntity.ok(UsuarioMapper.toListResponseUsuarioDto(users));
    }

    @Operation(summary = "Deletar usuário pelo id", description = "Deletar usuário pelo id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário deletado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable Long id){
         usuarioService.deletarUsuario(id);
         return ResponseEntity.ok("Usuário deletado com sucesso!");
    }
}
