package com.shopconnect.ms_inventario.Dto;

public class InventarioDTO {
    
    private Long idInventario;
    private Integer stockActual;

    // Constructor Vacío para Spring Boot
    public InventarioDTO() {
    }

    // Constructor con Parámetros que usa el controlador
    public InventarioDTO(Long idInventario, Integer stockActual) {
        this.idInventario = idInventario;
        this.stockActual = stockActual;
    }

    // Getters y Setters
    public Long getIdInventario() { return idInventario; }
    public void setIdInventario(Long idInventario) { this.idInventario = idInventario; }
    public Integer getStockActual() { return stockActual; }
    public void setStockActual(Integer stockActual) { this.stockActual = stockActual; }
}
