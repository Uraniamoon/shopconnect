package com.shopconnect.ms_pagos.Dto;

public class PagoDTO {
    
    private Long idPedido;
    private Double monto;

    // Constructor Vacío
    public PagoDTO() {
    }

    // Constructor con Parámetros
    public PagoDTO(Long idPedido, Double monto) {
        this.idPedido = idPedido;
        this.monto = monto;
    }

    // Getters y Setters
    public Long getIdPedido() { return idPedido; }
    public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }
    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }
}