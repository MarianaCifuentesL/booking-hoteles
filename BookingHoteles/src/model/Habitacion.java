package model;

public class Habitacion {
    private String tipo;
    private double precio;
    private String caracteristicas;
    private int capacidad;

    public Habitacion(String tipo, double precio, String caracteristicas, int capacidad) {
        this.tipo = tipo;
        this.precio = precio;
        this.caracteristicas = caracteristicas;
        this.capacidad = capacidad;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public void mostrarDetalles() {
        System.out.println("Tipo: " + tipo + " | Precio: " + precio + " | Características: " + caracteristicas);
    }

    // Métodos Getters y Setters
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(String caracteristicas) {
        this.caracteristicas = caracteristicas;
    }
}



