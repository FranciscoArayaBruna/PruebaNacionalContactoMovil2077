package com.pancho.contactomovil2077.Modelo;

public class Contacto {
    private String nombre;
    private String correo;

    public Contacto(String nombre, String correo) {
        this.nombre = nombre;
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }
}
