package com.shopconnect.ms_pagos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Respuesta estandar de error del microservicio de pagos")
public class ErrorResponseDTO {

    @Schema(description = "Mensaje legible que explica el error ocurrido", example = "Pago no encontrado")
    private String mensaje;

    @Schema(description = "Fecha y hora en que ocurrio el error")
    private LocalDateTime fechaHora;

    public ErrorResponseDTO() {}

    public ErrorResponseDTO(String mensaje) {
        this.mensaje = mensaje;
        this.fechaHora = LocalDateTime.now();
    }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
}
