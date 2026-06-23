package com.shopconnect.ms_inventario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;

@Schema(description = "Datos de entrada/salida del inventario")
public class InventarioDTO {

    @Schema(description = "Identificador unico del inventario generado por Oracle", example = "1")
    private Long idInventario;

    @PositiveOrZero(message = "El stock no puede ser negativo")
    @Schema(description = "Cantidad actual en stock", example = "50")
    private Integer stockActual;

    public InventarioDTO() {}

    public InventarioDTO(Long idInventario, Integer stockActual) {
        this.idInventario = idInventario;
        this.stockActual = stockActual;
    }

    public Long getIdInventario() { return idInventario; }
    public void setIdInventario(Long idInventario) { this.idInventario = idInventario; }
    public Integer getStockActual() { return stockActual; }
    public void setStockActual(Integer stockActual) { this.stockActual = stockActual; }
}
