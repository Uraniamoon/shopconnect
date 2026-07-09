package com.shopconnect.ms_pedidos.dto;

import jakarta.validation.constraints.NotBlank;

public class EstadoUpdateDTO {

    @NotBlank(message = "El estado es obligatorio")
    private String estado;

    public EstadoUpdateDTO() {}

    public EstadoUpdateDTO(String estado) {
        this.estado = estado;
    }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
