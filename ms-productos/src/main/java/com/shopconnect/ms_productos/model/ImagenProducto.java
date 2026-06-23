package com.shopconnect.ms_productos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "imagenProducto")
public class ImagenProducto {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_imagen;

    @NotBlank(message = "La URL de la imagen es obligatoria")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    @Column(nullable = false, unique = true, length = 100)
    private String url;

    @NotNull(message = "La orden del producto es obligatorio")
    @DecimalMin(value = "0", inclusive = false, message = "Debe contener número positivo")
    @Column(nullable = false)
    private Integer orden;

    @Column(nullable = false)
    private boolean principal;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;


    public ImagenProducto(){}

    public ImagenProducto(String url, Integer orden, boolean principal){
        this.url = url;
        this.orden = orden;
        this.principal = principal;
    }

    public Long getId() { return id_imagen; }
    public void setId(Long id_imagen) { this.id_imagen = id_imagen; }
    
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public Integer getOrden() { return orden; }
    public void setOrden(Integer orden) { this.orden = orden; }

    public boolean getPrincipal() { return principal; }
    public void setPrincipal(boolean principal) { this.principal = principal; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    @Override
    public String toString(){
        return "ImagenProducto{id=" + id_imagen +
        ", url='" + url +
        "', orden='" + orden +
        "', principal='" + principal + "}";
    }


    
}
