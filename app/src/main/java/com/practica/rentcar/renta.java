package com.practica.rentcar;

public class renta {

    private String renta_numero;
    private String renta_placa;
    private String renta_usuario;
    private String fecha_inicial;
    private String fecha_final;

    public renta(){

    }

    public String getRenta_numero() { return renta_numero; }
    public void setRenta_numero(String renta_numero) { this.renta_numero = renta_numero; }

    public String getRenta_placa() { return renta_placa; }
    public void setRenta_placa(String renta_placa) { this.renta_placa = renta_placa; }

    public String getRenta_usuario() { return renta_usuario; }
    public void setRenta_usuario(String renta_usuario) { this.renta_usuario = renta_usuario; }

    public String getFecha_inicial() { return fecha_inicial; }
    public void setFecha_inicial(String fecha_inicial) { this.fecha_inicial = fecha_inicial; }

    public String getFecha_final() { return fecha_final; }
    public void setFecha_final(String fecha_final) { this.fecha_final = fecha_final; }

}
