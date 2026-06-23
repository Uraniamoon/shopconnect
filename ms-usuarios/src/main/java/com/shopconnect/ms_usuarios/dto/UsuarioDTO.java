package com.shopconnect.ms_usuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos de entrada/salida de un usuario")
public class UsuarioDTO {

    @Schema(description = "Identificador unico del usuario generado por Oracle", example = "1")
    private Long idUsuario;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    @Schema(description = "Nombre completo del usuario", example = "Juan Perez")
    private String nombre;

    @NotBlank(message = "El correo electronico es obligatorio")
    @Email(message = "El correo debe tener formato valido")
    @Size(max = 150, message = "El correo no puede superar 150 caracteres")
    @Schema(description = "Correo electronico unico del usuario", example = "juan@mail.com")
    private String correoElectronico;

    public UsuarioDTO() {}

    public UsuarioDTO(Long idUsuario, String nombre, String correoElectronico) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correoElectronico = correoElectronico;
    }

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }
}
