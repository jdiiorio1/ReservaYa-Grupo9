package com.example.reservaya;

public class HistorialReserva {

   // private String complejo;
   // private int imagen_complejo;
  //  private String direccion;
    //private String id;

    private String NombreComplejo;
    private String capacidad;
    private String techada;
    private String fechaReserva;

    private String horaReserva;
    private String costo;


    public HistorialReserva(String nombreComplejo, String capacidad, String techada, String fechaReserva, String horaReserva, String costo) {
        NombreComplejo = nombreComplejo;
        this.capacidad = capacidad;
        this.techada = techada;
        this.fechaReserva = fechaReserva;
        this.horaReserva = horaReserva;
        this.costo = costo;
    }

    /*
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    */
    public String getNombreComplejo() {
        return NombreComplejo;
    }

    public void setNombreComplejo(String nombreComplejo) {
        NombreComplejo = nombreComplejo;
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

    public String getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(String fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public String getHoraReserva() {
        return horaReserva;
    }

    public void setHoraReserva(String horaReserva) {
        this.horaReserva = horaReserva;
    }

    public String getCosto() {
        return costo;
    }

    public void setCosto(String costo) {
        this.costo = costo;
    }
}
