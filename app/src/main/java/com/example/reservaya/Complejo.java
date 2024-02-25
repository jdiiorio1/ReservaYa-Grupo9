package com.example.reservaya;

public class Complejo {

    private String id;
    private String nombre;
    private int imagenComplejo;
    private String direccion;
    private String canchasDisponibles;
    private String distancia;

    public Complejo(String id, String nombre, int imagenComplejo, String direccion, String canchasDisponibles, String distancia) {
        this.id = id;
        this.nombre = nombre;
        this.imagenComplejo = imagenComplejo;
        this.direccion = direccion;
        this.canchasDisponibles = canchasDisponibles;
        this.distancia = distancia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getImagenComplejo() {
        return imagenComplejo;
    }

    public void setImagen_complejo(int imagenComplejo) {
        this.imagenComplejo = imagenComplejo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCanchasDisponibles() {
        return canchasDisponibles;
    }

    public void setCanchasDisponibles(String canchasDisponibles) {
        this.canchasDisponibles = canchasDisponibles;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDistancia() {
        return distancia;
    }

    public void setDistancia(String distancia) {
        this.distancia = distancia;
    }
}
