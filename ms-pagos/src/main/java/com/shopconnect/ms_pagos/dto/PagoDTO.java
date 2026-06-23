package com.shopconnect.ms_pagos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Datos de entrada/salida de un pago")
public class PagoDTO {

    @NotNull(message = "El ID del pedido es obligatorio")
    @Schema(description = "Identificador del pedido asociado al pago", example = "1")
    private Long idPedido;

    @Positive(message = "El monto debe ser mayor a cero")
    @Schema(description = "Monto del pago", example = "150.00")
    private Double monto;

    public PagoDTO() {}

    public PagoDTO(Long idPedido, Double monto) {
        this.idPedido = idPedido;
        this.monto = monto;
    }

    public Long getIdPedido() { return idPedido; }
    public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }
    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }
}
