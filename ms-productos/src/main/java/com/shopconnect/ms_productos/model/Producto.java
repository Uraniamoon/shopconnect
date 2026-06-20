package com.shopconnect.ms_productos.model;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_producto;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 100, message = "El nombre no debe superar los 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotNull(message = "El precio del producto es obligatorio")
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal precio;

    @NotNull(message = "El stock del producto es obligatorio")
    @DecimalMin(value = "0", inclusive = false, message = "Debe contener número positivo")
    @Column(nullable = false)
    private Integer stock;

    @NotBlank(message = "El SKU del producto es obligatorio")
    @Size(max = 50)
    @Column(nullable = false, unique = true, length = 50)
    private String sku;

    //Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_marca", nullable = false)
    private Marca marca;

    @OneToMany(mappedBy = "producto")
    private List<ImagenProducto> imagenes;


    //Getters and setters
    public Producto(){}

    public Long getId(){return id_producto;}
    public void setId(Long id_producto) {this.id_producto = id_producto;}

    public String getNombre(){return nombre;}
    public void setNombre(String nombre){this.nombre = nombre;}

    public BigDecimal getPrecio(){return precio;}
    public void setPrecio(BigDecimal precio){this.precio = precio;}

    public Integer getStock(){return stock;}
    public void setStock(Integer stock){this.stock = stock;}

    public String getSku(){return sku;}
    public void setSku(String sku){this.sku = sku;}

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public Marca getMarca() { return marca; }
    public void setMarca(Marca marca) { this.marca = marca; }

    public List<ImagenProducto> getImagenes() { return imagenes; }
    public void setImagenes(List<ImagenProducto> imagenes) { this.imagenes = imagenes; }


    @Override
    public String toString(){
        return "Producto{id=" + id_producto 
        + ",nombre='" + nombre 
        + "',precio=" + precio 
        + ",stock=" + stock 
        + ",sku='" + sku + "'}";}

    
}