package com.shopconnect.ms_productos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos de entrada/salida de un producto")
public class ProductoDTO {

    @Schema(description = "Identificador unico del producto generado por Oracle", example = "1")
    private Long idProducto;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    @Schema(description = "Nombre del producto", example = "Laptop HP")
    private String nombre;

    @Positive(message = "El precio debe ser mayor a cero")
    @Schema(description = "Precio de venta del producto", example = "999.99")
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
