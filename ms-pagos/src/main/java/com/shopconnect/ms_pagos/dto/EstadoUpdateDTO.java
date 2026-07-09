package com.shopconnect.ms_pagos.dto;

public class EstadoUpdateDTO {

    private String estado;

    public EstadoUpdateDTO() {}

    public EstadoUpdateDTO(String estado) {
        this.estado = estado;
    }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
