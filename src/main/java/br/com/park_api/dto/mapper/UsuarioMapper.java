package br.com.park_api.dto.mapper;

import br.com.park_api.dto.UsuarioRequestDto;
import br.com.park_api.dto.UsuarioResponseDto;
import br.com.park_api.entity.Usuario;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.List;
import java.util.stream.Collectors;

public class UsuarioMapper {
    public static Usuario toUsuario(UsuarioRequestDto usuarioRequestDto){
        return new ModelMapper().map(usuarioRequestDto, Usuario.class);
    }

    public static UsuarioResponseDto toUsuarioResponseDto(Usuario usuario){
        String role = usuario.getRole().name().substring("ROLE_".length());
        PropertyMap<Usuario, UsuarioResponseDto> props = new PropertyMap<Usuario, UsuarioResponseDto>() {
            @Override
            protected void configure() {
                map().setRole(role);
            }
        };
        ModelMapper mapper = new ModelMapper();
        mapper.addMappings(props);
        return mapper.map(usuario, UsuarioResponseDto.class);
    }

    public static List<UsuarioResponseDto> toListResponseUsuarioDto(List<Usuario> usuarios){
        return usuarios.stream().map(usuario -> toUsuarioResponseDto(usuario)).collect(Collectors.toList());
    }
}
