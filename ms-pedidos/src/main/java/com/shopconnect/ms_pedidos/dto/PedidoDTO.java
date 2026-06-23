package com.shopconnect.ms_pedidos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;

@Schema(description = "Datos de entrada/salida de un pedido")
public class PedidoDTO {

    @Schema(description = "Identificador unico del pedido generado por Oracle", example = "1")
    private Long idPedido;

    @Positive(message = "El total debe ser mayor a cero")
    @Schema(description = "Monto total del pedido", example = "250.00")
    private Double total;

    @Schema(description = "Estado actual del pedido", example = "PENDIENTE_PAGO")
    private String estado;

    public PedidoDTO() {}

    public PedidoDTO(Long idPedido, Double total, String estado) {
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
