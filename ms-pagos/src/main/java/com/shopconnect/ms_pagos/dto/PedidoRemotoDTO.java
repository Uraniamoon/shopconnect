package com.shopconnect.ms_pagos.dto;

public class PedidoRemotoDTO {

    private Long idPedido;
    private Double total;
    private String estado;

    public PedidoRemotoDTO() {}

    public PedidoRemotoDTO(Long idPedido, Double total, String estado) {
        this.idPedido = idPedido;
        this.total = total;
        this.estado = estado;
    }

    public Long getIdPedido() { return idPedido; }
    public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }
    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
