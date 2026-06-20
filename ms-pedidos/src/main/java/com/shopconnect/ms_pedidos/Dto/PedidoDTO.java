package com.shopconnect.ms_pedidos.Dto;

public class PedidoDTO {
    
    private Long idPedido;
    private Double total;
    private String estado;

    // Constructor Vacío para Spring Boot
    public PedidoDTO() {
    }

    // Constructor con parámetros para construirlo rápido
    public PedidoDTO(Long idPedido, Double total, String estado) {
        this.idPedido = idPedido;
        this.total = total;
        this.estado = estado;
    }

    // Getters y Setters
    public Long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}