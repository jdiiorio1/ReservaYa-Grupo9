package com.example.reservaya;

public class Cancha {

   // private String complejo;
   // private int imagen_complejo;
  //  private String direccion;
    private String id;
    private String capacidad;
    private String techada;

    public Cancha(String id, String capacidad, String techada) {
     //   this.complejo = complejo;
     //   this.direccion = direccion;
        this.id = id;
        this.capacidad = capacidad;
        this.techada = techada;
      //  this.imagen_complejo = imagen_complejo;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(String capacidad) {
        this.capacidad = capacidad;
    }

    public String getTechada() {
        return techada;
    }

    public void setTechada(String techada) {
        this.techada = techada;
    }


}
