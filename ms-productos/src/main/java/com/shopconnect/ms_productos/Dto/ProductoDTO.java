package com.shopconnect.ms_productos.Dto;

public class ProductoDTO {
    private Long idProducto;
    private String nombre;
    private Double precioVenta;

    public ProductoDTO() {}

    public ProductoDTO(Long idProducto, String nombre, Double precioVenta) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precioVenta = precioVenta;
    }

    public Long getIdProducto() { return idProducto; }
    public void setIdProducto(Long idProducto) { this.idProducto = idProducto; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Double getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(Double precioVenta) { this.precioVenta = precioVenta; }
}